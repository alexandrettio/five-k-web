(defproject five-k-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-time "0.12.0"]
                 [org.clojure/java.jdbc "0.6.2-alpha3"]
                 [org.postgresql/postgresql "9.4.1211"]
                 [honeysql "0.8.1"]]
  :main ^:skip-aot five-k-api.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
