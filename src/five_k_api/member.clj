(ns five-k-api.member
  (:require [honeysql.core :as sql]))

;; table key -- :members

(def db-scheme [[:id :serial "primary key"]
                [:first-name :text]])

