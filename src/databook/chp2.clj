;;
;; CHAPTER 2 - Cleaning and validating
;;

(ns databook.chp2
    (:require [clojure.string :as string])
    (:use 
        [clojure.string :only [upper-case]]
        [clj-diff.core]))

;; identification of duplicates
;; here you could look as assigning unique hashes for every value/row...

(def fuzzy-max-diff 2)
(def fuzzy-percent-diff 0.1)
(def fuzzy-dist edit-distance)

(defn fuzzy=
    "returns a fuzzy match"
    [a b]
    (let [dist (fuzzy-dist a b)]
        (or (<= dist fuzzy-max-diff)
            (<= (/ dist (min (count a) (count b)))
                (fuzzy-percent-diff)))))    


(defn records-match?
    [key-fn a b]
    (let [
        kfns (if (sequential? key-fn) key-fn [key-fn])
        rfn (fn [prev next-fn]
                    (and prev 
                        (fuzzy= (next-fn a)
                        (next-fn b))))]
    (reduce rfn true kfns)))

(def data
   {:mulder  {:given-name "Fox" :surname "Mulder"}
    :molder  {:given-name "Fox" :surname "Molder"}
    :mulder2 {:given-name "fox" :surname "mulder"}
    :scully  {:given-name "Dana" :surname "Scully"}
    :scully2 {:given-name "Dan" :surname "Scully"}})

;; synonym maps ... isn't this were most people 
;; start with maps?
(def state-syns {
    "ALABAMA" "AL"
    "ALASKA"  "AK",
    "ARIZONA" "AZ"
    "TEXAS" "BIBLE-THUMPERS"})

(defn normalise-state
    [state]
    (let [uc-state (upper-case state)]
        ;;if the value can't be found return the uppercase input
        (state-syns uc-state uc-state)))

;; cleaning with REGEXs
;; first define a regular expression for parsing US numbers
(def phone-regex
    ;;(?x) allows us to place the regex across multiple lines
    #"(?x)
    (\d{3})
    \D{0,2}
    (\d{3})
    \D?
    (\d{4})
    ")


;; given something that looks like xxx-yyy zzzz
;; will return (xxx)yyy-zzzz
(defn clean-us-phone-number
    [number]
    (if-let [[_ area-code prefix post]
        (re-find phone-regex number)]
        (str \( area-code \) prefix \- post)))

