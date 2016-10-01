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
  [table-name columns-def]
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

;; deprecated
(defn ->ddl
  [[tbl-name {:keys [columns]}]]
  (create-table tbl-name columns))

(defn create-scheme!
  [db]
  (->>
   db-spec
   (sort-by #(-> % second :order))
   (map (fn [[n c]] [n (:columns c)]))
   (map (partial apply create-table))
   (run-ddls! db)))

(defn drop-scheme!
  [db]
  (->>
   db-spec
   (sort-by #(-> % second :order))
   keys
   reverse
   (map name)
   (map ->psql-name)
   (map (partial format "DROP TABLE IF EXISTS %s "))
   (run-ddls! db)))

(defn with-reset-db
  [db]
  (fn [f]
    (drop-scheme! db)
    (create-scheme! db)
    (f)))

(use-fixtures :each
  (with-reset-db db))

(deftest members
  (testing "insert"
    (do
      (jdbc/insert! db (name :members) {:first-name "test"} {:entities ->psql-name}))
    (is true)))
