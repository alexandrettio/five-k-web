(ns five-k-api.sql-utils
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.string :as s]))

(defn ->psql-name
  [s]
  (s/replace s #"-" "_"))

(defn create-table
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
   (map (fn [[n c]] [n (:columns c)]))
   (map (partial apply create-table))
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

