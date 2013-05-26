(defproject databook "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["conjars.org" "http://conjars.org/repo"]]
  :dependencies [
                 [org.clojure/clojure "1.5.1"]
                 [incanter/incanter-core "1.4.1"]
                 [incanter/incanter-io "1.4.1"]
                 [org.clojure/data.csv "0.1.2"]
                 [org.clojure/data.json "0.2.1"]
                 [incanter/incanter-excel "1.4.1"]
                 [org.clojure/java.jdbc "0.2.3"]
                 [org.xerial/sqlite-jdbc "3.7.2"]
                 [clj-diff "1.0.0-SNAPSHOT"]
                 ;;cascalog stuff
                 [cascalog "1.10.0"]
                 [org.slf4j/slf4j-api "1.7.2"]
                 ]
  :profiles
    {:dev
      {:dependencies
        [[org.apache.hadoop/hadoop-core "1.1.1"]]}}

  :main databook.core)
