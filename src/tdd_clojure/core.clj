(ns tdd-clojure.core)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn times2 [x] (do (println "Hello multiplier") (* x 2)))
