(ns tdd-clojure.contract-test
  (:require [clojure.string :as str]
            [clojure.test :refer :all]))

;; Example implementation of the greet function
(defn greet1
  ([] "Hello, World")
  ([name] (str "Hello, " name)))

;; Another way to do a greet function
(defn greet2 [name]
  (str/join " " ["Hello," name]))

;; Greet using fmt
(defn greet3 [name]
  (format "Hello, %s" name))

;; Greet using replace (dumb but fun)
(defn greet4 [name]
  (str/replace "Hello, WAT" #"WAT" name))

(defn greet-contract [greet-fn]
  (let [name "Chris"]
    (is (= (greet-fn name) (str "Hello, " name)))))

(defn greet-contract-multi-arity [greet-fn]
  (let [name "Chris"]
    (is (= (greet-fn name) (str "Hello, " name)))
    (is (= (greet-fn) (str "Hello, World")))))
(deftest greet1-test "Test greet1"
                     (greet-contract greet1) (greet-contract-multi-arity greet1))
(deftest greet2-test "Test greet2" (greet-contract greet2))
(deftest greet3-test "Test greet3" (greet-contract greet3))
(deftest greet4-test "Test greet3" (greet-contract greet4))