(defproject five-k-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-time "0.12.0"]
                 [mount "0.1.10"]
                 [funcool/clojure.jdbc "0.9.0"]
                 [org.postgresql/postgresql "9.4.1211"]
                 [honeysql "0.8.1"]]
  :main ^:skip-aot five-k-api.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
