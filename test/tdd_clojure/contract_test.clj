(ns tdd-clojure.contract-test
  (:require [clojure.string :as str]
            [clojure.test :refer :all]))

(defn test-greet-contract [greet-fn]
  (testing "greet function should return a greeting with the given name"
    (let [name "Chris"]
      (is (= (greet-fn name) (str "Hello, " name))))))

;; Example implementation of the greet function
(defn greet1 [name]
  (str "Hello, " name))

;; Another way to do a greet function
(defn greet2 [name]
  (str/join " " ["Hello," name]))

;; Greet using fmt
(defn greet3 [name]
  (format "Hello, %s" name))

;; Greet using replace (dumb but fun)
(defn greet4 [name]
  (str/replace "Hello, WAT" #"WAT" name))

;; Test the example implementation using the contract test
(test-greet-contract greet1)
(test-greet-contract greet2)
(test-greet-contract greet3)
(test-greet-contract greet4)