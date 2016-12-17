(ns five-k-api.core
  (:require [clj-time.core :as t])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(def bet-states #{:draft :published})

(defn bet
  [{:keys [id] :as member} start-at finish-at]
  {:owner-id id
   :start-at start-at
   :finish-at finish-at
   :state :draft})

(defn max-coll
  [seed coll]
  (if (empty? coll)
    seed
    (reduce max coll)))

(defn save-bet!
  [db bet]
  (let [new-id (->> @db
                    keys
                    (max-coll 0)
                    inc)
        new-bet (merge bet {:id new-id})]
    (swap! db assoc new-id new-bet)))

(defn db-values
  [db]
  (map (fn [[_ value]] value) db))

(defn exists-bet
  [db id]
  (->> db
       db-values
       (map :id)
       (some #{id})
       boolean))

(defn publish-bet!
  [db id]
  (when (exists-bet @db id)
    (swap! db update id assoc :state :published)))

(defn list-published-bets
  [db]
  (->> db
       db-values
       (filter #(-> % :state (= :published)))))

(defn gen-id!
  []
  (let [counter (atom 0)]
    (fn []
      (swap! counter inc))))

(def member-id (gen-id!))
