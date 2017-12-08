(ns tdd-clojure.state_test
  (:require [clojure.test :refer :all]))

;; atoms - synchronised, each change happens completely before next one is applied. Uncoordinated. So not great for complex systems with concurrent updates but no transactions means it's faster.

(deftest atoms "When data is observed by > 1 thread you need to protect them from uncordinated updates"
               (let [x (atom 0)]
                 (is (= 0 @x))
                 (is (= 1 (do (swap! x inc) @x)))))

(deftest watching "Watches can apply to all reference types, like observer."
                  (let [x (atom 0)
                        reset (fn [key atom old new] (if (> new 2) (reset! atom 0)))]
                    (is (= 0 (do (add-watch x :key reset) (swap! x inc) (swap! x inc) (swap! x inc) @x)))))

;; refs - transactional.

(deftest refs "Do transactions using ref-set. Refs can _only_ be changed inside a transaction"
              (let [my-val (ref 0)
                    number-changes (ref 0)
                    update (fn [new] (dosync
                               (ref-set my-val new)
                               (ref-set number-changes (inc @number-changes))))]
                (is (= 2 (do (update 10) (update 20) @number-changes)))))