(ns five-k-api.member-test
  (:require [five-k-api.member :as m]
            [five-k-api.test-utils :as tu]
            [five-k-api.sql :as sql]
            [clojure.java.jdbc :as jdbc]
            [clojure.test :refer :all]))

(use-fixtures :each
  (tu/with-reset-db))

(deftest members
  (testing "insert"
    (do
      (jdbc/insert! sql/db (name :members) {:first-name "uasya"} {:entities ->psql-name}))
    (is true)))
