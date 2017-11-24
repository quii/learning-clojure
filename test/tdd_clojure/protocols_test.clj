(ns tdd_clojure.protocols-test
  (:require [clojure.test :refer :all]
            [tdd_clojure.protocols :refer :all]))

(deftest protocols "protocols are just like java interfaces"
                  (testing "animals shouting"
                    (is (= "meow!!!" (shout (->Animal "cat" "meow")))))

                   (testing "but you can use them on anything using extend-protocol, yay"
                     (is (= "MEOW" (shout "meow")))))