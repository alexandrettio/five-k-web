(ns five-k-api.utils.ddl
  (:require [clojure.string :as str]))

;; DDL copied from clojure.java.jdbc

(defn as-sql-name
  "Given a naming strategy function and a keyword or string, return
   a string per that naming strategy.
   A name of the form x.y is treated as multiple names, x, y, etc,
   and each are turned into strings via the naming strategy and then
   joined back together so x.y might become `x`.`y` if the naming
   strategy quotes identifiers with `."
  [f x]
  (let [n (name x)
        i (.indexOf n (int \.))]
    (if (= -1 i)
      (f n)
      (str/join "." (map f (.split n "\\."))))))

(defn create-table-ddl
  "Given a table name and a vector of column specs, return the DDL string for
  creating that table. Each column spec is, in turn, a vector of keywords or
  strings that is converted to strings and concatenated with spaces to form
  a single column description in DDL, e.g.,
    [:cost :int \"not null\"]
    [:name \"varchar(32)\"]
  The first element of a column spec is treated as a SQL entity (so if you
  provide the :entities option, that will be used to transform it). The
  remaining elements are left as-is when converting them to strings.
  An options map may be provided that can contain:
  :table-spec -- a string that is appended to the DDL -- and/or
  :entities -- a function to specify how column names are transformed."
  ([table specs] (create-table-ddl table specs {}))
  ([table specs opts]
   (let [table-spec     (:table-spec opts)
         entities       (:entities   opts identity)
         table-spec-str (or (and table-spec (str " " table-spec)) "")
         spec-to-string (fn [spec]
                          (try
                            (str/join " " (cons (as-sql-name entities (first spec))
                                                (map name (rest spec))))
                            (catch Exception _
                              (throw (IllegalArgumentException.
                                      "column spec is not a sequence of keywords / strings")))))]
     (format "CREATE TABLE %s (%s)%s"
             (as-sql-name entities table)
             (str/join ", " (map spec-to-string specs))
             table-spec-str))))
