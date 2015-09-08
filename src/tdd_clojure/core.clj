(ns tdd-clojure.core)

(require `tdd-clojure.some-lib.stuff) ; loads the file
(refer `tdd-clojure.some-lib.stuff) ; loads the namespace into here so we dont have to fully qualify it

(defn times2 [x] (do (println "Hello multiplier") (* x 2)))
 
(defn a-cool-library-function [a] (my-library-adder a))

(defn recursive-with-seq
  ([collection total] (if (seq collection) (recur (rest collection) (+ total (first collection))) total))
  ([collection] (recursive-with-seq collection 0)))

; need to see how this works, potentially cool though!
(defn a-function-with-a-test-in-it
  {:test (fn [] (assert (=(a-function-with-a-test-in-it) true)))}
  [] (true))
