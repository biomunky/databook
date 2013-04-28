(ns databook.core
  (:gen-class)
  (:use 
    [databook.chp1 :as chp1]
    [clojure.zip :exclude [next replace remove]]
    [databook.chp2 :as chp2]
    ))

(defn -main [& args] 

    ;;
    ;; CHAPTER 1
    ;; excluded the html scraping and RDF/Sparql stuff
    ;; it holds 0 interest for me at this time
    ;;

    ;;print the initial data
    #_(println some-data)

    ;; print some json
    #_(println some-json)

    ;; do something with the json data & data collection
    #_(println (:rows some-json))
    #_(println people)

    ;;read some data from xls
    #_(println xls-data)

    ;;read some data from an sqlite DB \o/
    #_(println db-data)
    #_(println (chp1/load-xml-data "resources/sample.xml" down right))

    ;;
    ;; CHAPTER 2 
    ;;

    ;;clean a 'murican phone number
    (println (clean-us-phone-number "123-345-2890"))
    (println (clean-us-phone-number "123 095 2890"))

    )