(ns databook.chp6
  (:use
   [incanter core datasets io]))

;;
;; This code will work if you copy and paste into a repl
;; you may need to change the imports
;;

(def iris (get-dataset :iris))

;; iris <- datasets::iris
;; names(iris)
(def cols (col-names iris))

(def numb-rows (nrow iris))

(def species ($ :Species iris))

;; convert clj data structure into incanter
(def a-matrix (to-dataset [[1 2 3] [4 5 6]]))

;; create a dataset with colnames
(def b-matrix
  (dataset [:name :age :gender]
           [["foo" 21 "m"] ["bar" 32 "f"]]))

;; view a dataset in a swing window
;;(view iris) ;; view takes any objext & tries to display it

;; read some data into Incanter
;; va-data <- read.csv("resource/all...", h=T) in R
;; class(va-data) > "data.frame"
(def data-file "resources/all_160_in_51.P35.csv")
(def va-data (read-dataset data-file :header true))
;; convert to matrix

(def va-matrix (to-matrix ($ [:POP100 :HU100 :P035001] va-data)))

;; can create like any clojure structure
(println (take 10 va-matrix))

;; use incanter operators to get the sum of each col
(def col-sums (reduce plus va-matrix))

;; get the average of the cols (there's probably a function for this)
(def avs (map #(/ % (nrow va-matrix)) (reduce plus va-matrix)))

;; there's a load of doc here: https://github.com/liebke/incanter/wiki/matrices

($= 7 * 7)
;; gah! infix notification
;; can use this to perform matrix math
(take 2 va-matrix)
(take 2 ($= va-matrix * 2))

;; to work only on the first two rows
($= (take 2 va-matrix) * 2)

;; can thus change the above average to
(def avs2 ($= (reduce plus va-matrix) / (count va-matrix)))

(def data-file "resources/all_160.P3.csv")
(def race-data (read-dataset data-file :header true))

;; select columns using $
(def state-county-pop ($ [:STATE :COUNTY :POP100] chp6/race-data))

;; this is fine for cols, what about rows?
(def rows-0-4 ($ [0 1 2 3] [:STATE :COUNTY :POP100] chp6/race-data))

;; rows 1 - 100 in steps of two
(def rows-even-1-100 ($ (apply vector (range 0 102 2)) [:STATE :COUNTY :POP100] chp6/race-data))

;; can filter datasets with $where
(def richmond ($where {:NAME "Richmond city"} chp6/va-data))

;; more data, this looks alittle like a mongo query!
(def small ($where {:POP100 {:lte 1000}} chp6/va-data))
(def medium ($where {:POP100 {:gt 1000 :lt 40000}} chp6/va-data))

;; standard operators :$gt :$lt :$gte :$lte

;; :$fn - allows you to define a function
(def random-data ($where {:GEOID {:$fn (fn [_] (< (rand) 0.5))}} chp6/va-data))

;; more can be found here: http://liebke.github.io/incanter/core-api.html#incanter.core/query-dataset
