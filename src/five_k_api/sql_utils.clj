(ns five-k-api.sql-utils
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.string :as s]))

(defn ->psql-name
  [s]
  (s/replace s #"-" "_"))

(defn create-table-sql
  [table-name columns-def]
  (jdbc/create-table-ddl
   (name table-name)
   columns-def
   {:entities ->psql-name}))

(defn run-ddls!
  [db ddls]
  (doseq [q ddls]
    (jdbc/execute! db q)))

(defn create-scheme!
  [db db-spec]
  (->>
   db-spec
   (sort-by #(-> % second :order))
   ;;(map (fn [[n c]] [n (:columns c)]))
   ;; and another variant =)
   ;; more clear explanation of what we do
   (map #(update-in % [1] :columns))
   (map (partial apply create-table-sql))
   (run-ddls! db)))

(defn drop-scheme!
  [db db-spec]
  (->>
   db-spec
   (sort-by #(-> % second :order))
   keys
   reverse
   (map name)
   (map ->psql-name)
   (map (partial format "DROP TABLE IF EXISTS %s "))
   (run-ddls! db)))

