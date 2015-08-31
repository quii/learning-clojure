(ns tdd-clojure.core)

(require `tdd-clojure.some-lib.stuff) ; loads the file
(refer `tdd-clojure.some-lib.stuff) ; loads the namespace into here so we dont have to fully qualify it

(defn times2 [x] (do (println "Hello multiplier") (* x 2)))
 
(defn a-cool-library-function [a] (my-library-adder a))
