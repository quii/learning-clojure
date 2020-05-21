(ns tdd-clojure.defrecords-test
  (:require [clojure.test :refer :all]))

(defrecord Person [name age])
(defmulti summary class)
(defmethod summary Person [p] (str (:name p) " is " (.age p) " years old") )


(deftest records "Records are like maps but you can add methods to them (and so they can satisfy interfaces. There are different ways of constructing them. Records will have a known type and can offer better performance than maps "
               (let [cj (->Person "CJ" 33)
                     ruth (map->Person {:name "Ruth" :age 33})
                     dan (Person. "DB" 34)]
                 (is (= 33 (.age cj)))
                 (is (= "Ruth is 33 years old" (summary ruth)))))