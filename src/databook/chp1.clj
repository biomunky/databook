;;
;; CHAPTER 1 - Importing data
;; excluded the html scraping and RDF/Sparql stuff
;; it holds 0 interest for me at this time
;;

(ns databook.chp1 
  (:use
    [incanter.core :as core]
    [incanter.io :as io]
    [clojure.data.json :as json :exclude [pprint read]]
    [incanter.excel :as excel]
    [clojure.java.jdbc :exclude [resultset-seq]]
    [clojure.zip :exclude [next replace remove]]
    [clojure.xml])
  
  (:require
    [clojure [string :as string]])
  
  (:import [java.net URL]))

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


;; an example of reading xml - seems far too complicated
;; who uses XML these days anyway
;; the 'cool' part here is the use of a zipper to navigate the structure
(defn load-xml-data [xml-file first-data next-data]
    (let [data-map (fn [node]
            [(:tag node) (first (:content node))])]
    ;;threading
    (->>
        ;; parse the xml data file
        (parse xml-file)
        xml-zip ;; the zipper

        ;;walk it to get nodes
        first-data ;; going down into the structure - gets all the people
        (iterate next-data) ;; now iterate over the people
        (take-while #(not (nil? %)))
        (map children)

        ;;convert nodes to a sequence of maps
        (map #(mapcat data-map %))
        (map #(apply array-map %))
        
        ;; make it into an incanter dataset
        core/to-dataset  
        )))
