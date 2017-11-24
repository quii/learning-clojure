(ns tdd_clojure.multimethods)

(defmulti shout :animal)

(defmethod shout :pig [_] "oink")
(defmethod shout :cat [_] "hisss")
(defmethod shout :dog [_] "bark")

(defmethod shout :default [_] "wtf")