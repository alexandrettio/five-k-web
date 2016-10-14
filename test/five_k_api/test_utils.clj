(ns five-k-api.test-utils
  (:require [five-k-api.utils.sql :as su]
            [five-k-api.sql :refer [db] :as sql]
            [mount.core :as mount]))

(defn wrap-mount
  []
  (fn [f]
    (mount/start)
    (f)
    (mount/stop)))

(defn with-reset-db
  []
  (fn [f]
    (su/drop-scheme! db sql/db-spec)
    (su/create-scheme! db sql/db-spec)
    (f)))
