(ns tdd-clojure.let-test
  (:require [clojure.test :refer :all]))

(deftest let-and-fn "Let allows you define values (includes functions obvs) that are only in scope for the proceeding function"
                    (is true
                        (let [is-5 (fn [x] (= x 5))]
                          (is-5 5))))

(deftest let-it-go "let variations"
  (letfn [(multiply-2 [x] (* x 2))]
    (is (= 20 (multiply-2 10)))))
