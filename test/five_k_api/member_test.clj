(ns five-k-api.member-test
  (:require [five-k-api.member :as m]
            [clojure.java.jdbc :as jdbc]
            [clojure.test :refer :all]))

(def db
  {:dbtype "postgresql"
   :dbname "ft"
   :host "localhost"
   :user "ft"
   :password "ft"})

(defn ->psql-name
  [s]
  (clojure.string/replace s #"-" "_"))

(defn create-table
  [columns-def table-name]
  (jdbc/create-table-ddl
   (name table-name)
   columns-def
   {:entities ->psql-name}))

(def projects-columns
  [[:id :serial "primary key"]
   [:name :text]
   [:member-id :integer (str "references " (name :members) " (id)")]])

(def db-spec
  {:members {:columns [[:id :serial "primary key"]
                       [:first-name :text]]
             :order 1}
   :projects {:columns projects-columns
              :order 2}})

(defn run-ddls!
  [db ddls]
  (doseq [q ddls]
    (jdbc/execute! db q)))

(defn ->ddl
  [spec]
  (let [tbl-name (first spec)
        config (second spec)
        cs-def (:columns config)]
    (create-table cs-def tbl-name)))

(defn create-sceheme!
  [db]
  (->>
   db-spec
   (map ->ddl)
   (run-ddls! db)))

(defn drop-scheme!
  [db]
  (->>
   db-spec
   keys
   reverse
   (map jdbc/drop-table-ddl)
   (run-ddls! db)))

(deftest migrate-cycle
  (testing "create and drop schema"
    (do
      (create-sceheme! db)
      (drop-scheme! db))
    (is true)))