(ns databook.core
  (:gen-class)
  (:use
    [incanter.core :as core]
    [incanter.io :as io]
    [clojure.data.json :as json :exclude [pprint read]]
    [incanter.excel :as excel]
    [clojure.java.jdbc :exclude [resultset-seq]]
    ))

;; read a standard csv file
(def some-data (io/read-dataset "resources/sample.csv"))

;; read some json into an incanter dataset
(def some-json 
    (core/to-dataset
        (read-json
            (slurp "resources/sample.json"))))

;; example of using getting stuff out of datasets
(def people 
    (map 
        #(let [{r :role f :firstname l :lastname} %1 ]
            (str "the role of " f " " l " is " r))
    (:rows some-json)))

(def xls-data
    (excel/read-xls "resources/sample-header.xls"))

;; reading data from a database - should really use Korma
;; Eric keeps it simple

(defn load-table-data
    "will read all data from a table"
    [db table]
    (let [sql (str "SELECT * FROM " table ";")]
        (with-connection db
            (with-query-results rs [sql]
                (core/to-dataset (doall rs))))))

(def db {
    :subprotocol "sqlite"
    :subname "resources/sample.sqlite"
    :classname "org.sqlite.JDBC"})

(def db-data (load-table-data db 'people))

(defn -main [& args] 
    ;;print the initial data
    ;;(println some-data)

    ;; print some json
    ;;(println some-json)

    ;; do something with the json data & data collection
    ;;(println (:rows some-json))
    ;;(println people)

    ;;read some data from xls
    ;;(println xls-data)

    ;;read some data from an sqlite DB \o/
    ;;(println db-data)

    
    )