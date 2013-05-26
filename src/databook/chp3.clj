(ns databook.chp3
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]))

(def total-housing-units (ref 0))
(def total-families (ref 0))


;; function from chapter 2.  It's lazy, reads the file
;; into a lazy sequence
(defn lazy-read-csv
  [csv-file]
  (let [in-file (io/reader csv-file)
        csv-seq (csv/read-csv in-file)
        lazy (fn lazy [wrapped]
               (lazy-seq
                (if-let [s (seq wrapped)]
                  (cons (first s) (lazy (rest s)))
                  (.close in-file))))]
    (lazy csv-seq)))

;; with head function - uses the first row to define the headers
;; probably find data clojure.data.csv already has this functionality

(defn with-header [coll]
  (let [headers (map keyword (first coll))]
    (map (partial zipmap headers) (next coll))))

;; data
(def input-data "resources/all_160_in_51.P35.csv")

(defn ->int [i] (Integer. i))

(defn sum-item
  ([fields] (partial sum-item fields))
  ([fields accum item]
     ;; get data from a line of the csv and convert the cell value to an Int
     ;; create a vector as output - containing the sum of values
     (mapv + accum (map ->int (map item fields)))))

;; sum overall
(defn sum-items
  [accum fields coll]
  (reduce (sum-item fields) accum coll))


(defn update-totals [fields items]
  (let [mzero (mapv (constantly 0) fields)
        [sum-hu sum-fams] (sum-items mzero fields items)]
    ;; now update the values in the refs
    (dosync (alter total-housing-units #(+ sum-hu %))
            (alter total-families #(+ sum-fams %)))))

(defn thunk-update
  [fields data-chunk]
  (fn [] (update-totals fields data-chunk)))

;; run the code
(defn main-ref
  ([data-file] (main-ref data-file [:HU100 :P035001] 5))
  ([data-file fields chunk-count]
     (doall (->>
             (lazy-read-csv data-file)
             with-header
             (partition-all chunk-count)
             (map (partial thunk-update)
             (map future-call)
             (map deref)))
            (float (/ @total-families @total-housing-units)))))


  ;;
  ;; DOING THE SAME WITH AGENTS
  ;;
  ;; agent <- sent & send-off
  ;; agent <- send + 4
  ;; @agent => x + 4
  ;;

  (defn accum-sums
    [a b] (mapv + a b))

  ;; takes pair of numbers and divides them - generates the ratio
  (defn div-vec [[a b]]
    (float (/ a b)))

  ;; makes working with agents easier - takes an agent and waits for all messages
  ;; in its queue to be processed

  (defn force-val [a]
    (await a) @a)

  ;; run the process
  (defn main-agent
    ([data-file] (main-agent data-file [:P035001 :HU100] 5 5))
    ([data-file fields agent-count chunk-count]
       (let [mzero (mapv (constantly 0) fields)
             ;;create the agents - set to 0
             agents (map agent (take agent-count (repeat mzero)))]
         (dorun
          (->> (lazy-read-csv data-file)
               with-header
               (partition-all chunk-count)
               (map #(send %1 sum-items fields %2)
                    (cycle agents))))
         (->> agents
              (map force-val)
              (reduce accum-sums mzero)
              div-vec))))

;; there's a chance that the agent tasks are being retried because there's
;; a clash - values are being updated at the same time.
;; this can be avoided by using commute
