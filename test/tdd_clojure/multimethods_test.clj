(ns tdd_clojure.multimethods-test
  (:require [clojure.test :refer :all]
            [tdd_clojure.multimethods :refer :all]))

(deftest dispatch
                  (testing "dispatching to known values"
                    (is (= "bark" (shout {:animal :dog}))))

                  (testing "to unknown values"
                    (is (= "wtf" (shout {:animal :alien})))))