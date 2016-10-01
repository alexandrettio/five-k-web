(ns five-k-api.member-test
  (:require [five-k-api.member :as m]
            [clojure.java.jdbc :as jdbc]
            [clojure.test :as t]))

(def db
  {:dbtype "postgresql"
   :dbname "ft"
   :host "localhost"
   :user "ft"
   :password "ft"})

(defn ->psql-name
  [s]
  (clojure.string/replace s #"-" "_"))

(defn create-members-table
  []
  (jdbc/create-table-ddl
   (name :members)
   [[:id :serial "primary key"]
    [:first-name :text]]
   {:entities ->psql-name}))

(defn create-projects-table
  []
  (jdbc/create-table-ddl
   (name :projects)
   [[:id :serial "primary key"]
    [:name :text]
    [:member-id :integer (str "references " (name :members) " (id)")]]
   {:entities ->psql-name}))

(def db-spec
  {:members create-members-table
   :projects create-projects-table})

(defn run-ddls!
  [db ddls]
  (doseq [q ddls]
    (jdbc/execute! db q)))

(defn run-fn
  [f]
  (f))

(defn create-sceheme!
  [db]
  (->>
   db-spec
   vals
   (map run-fn)
   (run-ddls! db)))

(defn drop-scheme!
  [db]
  (->>
   db-spec
   keys
   reverse
   (map jdbc/drop-table-ddl)
   (run-ddls! db)
   ))

