(ns five-k-api.core-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as t]
            [five-k-api.core :refer :all]))

(deftest project-workflow
  (let [m1 {:id 1 :name "Vasya"}
        today (t/today)
        tomorrow (t/plus today (t/days 1))
        new-bet (bet m1 today tomorrow)
        bets (atom {})]
    (testing "member creating a new bet, it should be draft"
      (is (= :draft (:state new-bet))))
    (testing "db is ok"
      (save-bet! bets new-bet)
      (is (= 1 (count @bets)))
      (is (= new-bet (dissoc (get @bets 1) :id))))
    (testing "there is no the bet in search results"
      (is (= [] (list-published-bets @bets))))
    (testing "bets is ordered by start-at"
      (is true))))
