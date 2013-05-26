;;
;; CHAPTER 5 - Data processing with cascalog
;;

(ns databook.chp5
    (:require cascalog [workflow :as w] [ops :as c] [vars :as v])
    (:use
        [clojure.string :as string]
        [cascalog.api]))
