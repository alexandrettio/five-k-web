(ns five-k-api.utils.sql
  (:require [jdbc.core :as jdbc]
            [clojure.string :as s]
            [five-k-api.utils.ddl :as su]
            [honeysql.core :as hsql]))

(defn ->psql-name
  [s]
  (s/replace s #"-" "_"))

(defn <-psql-name
  [s]
  (s/replace s #"_" "-"))

(defn create-table-sql
  [table-name columns-def]
  (su/create-table-ddl
   (name table-name)
   columns-def
   {:entities ->psql-name}))

(defn run-ddls!
  [db ddls]
  (doseq [q ddls]
    (jdbc/execute db q)))

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

(defn insert-sql
  [spec]
  (-> (hsql/format spec)
      (update-in [0] str " returning id;")))

(defn select-sql
  [projection target condition]
  (hsql/format {:select projection
                 :from target
                 :where condition}))

(defn fetch
  ([db q]
   (fetch db q {}))
  ([db q opts]
   (jdbc/fetch db q (merge opts {:identifiers <-psql-name}))))

(defn fetch-one
  ([db q]
   (fetch-one db q {}))
  ([db q opts]
   (jdbc/fetch-one db q (merge opts {:identifiers <-psql-name}))))

(defn create-user
  [db username]
  (->>
    {:insert-into :members
     :values [{:first-name username}]}
    insert-sql
    (fetch-one db)))

(defn get-users-byname
  [db username]
  (->>
    (select-sql [:*] [:members] [:= :first-name username])
    (fetch db)))
(defn get-user-byid
  [db id]
  (->>
    (select-sql [:*] [:members] [:= :id id])
    (fetch-one db)))
