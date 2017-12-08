(ns tdd-clojure.threading_macro_test
  (:require [clojure.test :refer :all]
            [tdd-clojure.core :refer :all]))

(deftest thread-first-macro "-> can help readability by removing nesting and useful for getting stuff out of maps. Inserts the item as the second item in the next form. (i.e first argument)"
                            (is (= "2" (str (inc 1))))
                            (is (= "2" (-> 1 inc str)))
                            (is (= "5" (-> 1 (+ 4) str)))
                            (is (= "bob" (-> {:a {:b {:c "bob"}}} :a :b :c ))))

(deftest thread-last-macro "->> inserts the items as the _last_ argument"
                           (is (= "hello, world" (->> "world" (str "hello, "))))
                           (is (= "worldhello, " (-> "world" (str "hello, "))))
                           (is (= "234" (->> [1 2 3] (map inc) (apply str)))))

(deftest some-macro "used for when nil is returned by any step, the further steps are not executed, (rather than an NPE or other weird behaviour)"
                    (let [data-with-age {:age 33} data-without-age {:name "Chris"}]
                      (is (= 34 (some-> data-with-age :age inc)))
                      (is (= nil (some-> data-without-age :age inc)))
                      (is (thrown? Exception (-> data-without-age :age inc)))))