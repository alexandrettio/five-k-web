(ns five-k-api.member-test
  (:require [five-k-api.member :as m]
            [five-k-api.test-utils :as tu]
            [five-k-api.sql :refer [db]]
            [five-k-api.utils.sql :as su]
            [mount.core :as mount]
            [jdbc.core :as jdbc]
            [honeysql.core :as hsql]
            [clojure.test :refer :all]))

(use-fixtures :each
  (tu/wrap-mount)
  (tu/with-reset-db))

(deftest members
  (testing "insert"
    (let [fname "vasy"
          id ((su/create-user db fname)
              :id)
          mem (->>
               {:select [:*]
                :from [:members]
                :where [:= :id id]}
               hsql/format
               (su/fetch-one db))]
      (is (= 1 id))
      (is (= {:id 1 :first-name fname} mem)))))
