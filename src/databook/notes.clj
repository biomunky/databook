(use '[cascalog.api])
(require '[cascalog.ops :as c])

(def data [[1 2] [1 3] [3 4] [3 6] [5 2] [5 9]])

(def add-sort
  (?<- [?x ?sum]
      (data ?x ?y)
      (:sort ?x)
      (c/sum ?y :> ?sum)))

(?<- (stdout) [?x ?sum] (data ?x ?y) (:sort ?x) (c/sum ?y :> ?sum))

(?<- (stdout) [?x ?y] (data ?x ?y))

;; run the stored query
(def m (<- [?x ?y] (data ?x ?y)))
(?- (stdout) add-sort)

(def n (<- [?x] (data ?x _)))

(?- (stdout) n)

(defmapop times-10 [x] (* 10 x))

(defn a-query [a-coll]
    (<- [?x ?y ?tt] (a-coll ?x ?y) (times-10 ?y :> ?tt)))

(?- (stdout) (a-query data))
