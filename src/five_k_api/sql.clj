(ns five-k-api.sql
  (:require [five-k-api.member :as m]))

;; conn

(def db
  {:dbtype "postgresql"
   :dbname "ft"
   :host "localhost"
   :user "ft"
   :password "ft"})

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
