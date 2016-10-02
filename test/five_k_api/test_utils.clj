(ns five-k-api.test-utils
  (:require [five-k-api.sql-utils :as su]
            [five-k-api.sql :as sql]))

(defn with-reset-db
  []
  (fn [f]
    (su/drop-scheme! sql/db sql/db-spec)
    (su/create-scheme! sql/db sql/db-spec)
    (f)))
