(ns five-k-api.member
  (:require [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]))

(def tbl "members")

{:id 1 :name "test"}

(def db
  {:dbtype "postgresql"
   :dbname "ft"
   :host "localhost"
   :user "ft"
   :password "ft"})

(jdbc/query pg-uri ["select * from members"] )

{:select [:*]
 :from [:members]
 :where [:and
         [:= :id 1]
         [:= :name "test"]]}

(sql/format {})


