(ns five-k-api.sql
  (:require [five-k-api.member :as m]
            [jdbc.core :as jdbc]
            [mount.core :refer [defstate]]))

;; conn

(def conn-spec
  {:vendor "postgresql"
   :name "ft"
   :port 5432
   :host "localhost"
   :user "ft"
   :password "ft"})

(defstate db
  :start (jdbc/connection conn-spec)
  :stop (.close db))

;; spec

(def projects-columns
  [[:id :serial "primary key"]
   [:name :text]
   [:member-id :integer (str "references " (name :members) " (id)")]])

(def db-spec
  {:members {:columns m/db-scheme
             :order 1}
   :projects {:columns projects-columns
              :order 2}})
