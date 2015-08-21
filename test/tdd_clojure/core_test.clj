(ns tdd-clojure.core-test
  (:require [clojure.test :refer :all]
            [tdd-clojure.core :refer :all]))

(deftest it-times-by-2 (testing
                           "It can multiply by 2"
                         (is (= 4 (times2 2)))))

(deftest concatination
  (testing "Concatenating strings"
    (is "Hello, world!" (str "Hello, " "world" "!"))))
