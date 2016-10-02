(ns five-k-api.member
  (:require [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]))

;; table key -- :members

(def db-scheme [[:id :serial "primary key"]
                [:first-name :text]])

