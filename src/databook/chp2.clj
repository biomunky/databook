;;
;; CHAPTER 2 - Cleaning and validating
;;

(ns databook.chp2
    (:require [clojure.string :as string]))

;; cleaning with REGEXs
;; first define a regular expression for parsing US numbers
(def phone-regex
    #"(?x)
    (\d{3})
    \D{0,2}
    (\d{3})
    \D?
    (\d{4})
    ")

(defn clean-us-phone-number
    [number]
    (if-let [[_ area-code prefix post]
        (re-find phone-regex number)]
        (str \( area-code \) prefix \- post)))

