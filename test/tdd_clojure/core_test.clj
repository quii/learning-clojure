(ns tdd-clojure.core-test
  (:require [clojure.test :refer :all]
            [tdd-clojure.core :refer :all]))

(deftest it-times-by-2 (testing
                           "It can multiply by 2"
                         (is (= 4 (times2 2)))))

; Basics

(deftest concatination
  (testing "Concatenating strings"
    (is "Hello, world!" (str "Hello, " "world" "!"))))

; Functions

(deftest functions "Calling functions is done by making a list where the first argument is the function you want to call. This is more consistent than non-lisps"
  (is (= false (= "BUM" "bum"))))

(defn add-five [x] (+ x 5))

(deftest creatingfunctions "defn Is like def from scala. It will be public to this namespace"
  (is (= 15 (add-five 10))))

(deftest let-and-fn "Let allows you define values (includes functions obvs) that are only in scope for the proceeding function"
  (is true
      (let [is-5 (fn [x] (= x 5))]
        (is-5 5))))

(deftest fn-sugar "There is some syntactic sugar for writing anonymous functions"
  (is (= 20 (#(* % 10) 2))))

(deftest fn-sugar-more-args "And like _.1 _.2 in scala you can do..."
  (is (= 20 (#(+ %1 %2) 15 5))))

(defn greeting "Greets you" [name] (str "Hello, " name))
; (doc greeting)

(defn arrity
  ([x] "One argument")
  ([x y] "Two arguments")
  ([x y & otherargs] "More than 2 arguments"))

(deftest function-arrity "Functions can have multiple arity"
  (is (= "One argument" (arrity "Hi"))))

(deftest composition "You can compose functions with the -> symbol. In this case i am combining the functions which add 2 and 5 respectively and then passing 1 through them"
  (is (= 8
         (let [x (fn [a] (+ a 2)) y (fn [a] (+ a 5))]
           (-> 1 x y)))))


; Collections
(deftest using-apply "Apply 'splats' a sequence of elements into a set of arguments to a function. Not like map which applies a function to each element in a collection to return a new collection"
  (is (= 6 (apply + [1 2 3]))))


; Macros is a neat feature of lisps and is enabled by the very uniform syntax and treating "code as data". A lot of the standard lib is built using macros

; Macros have to return a list
(defmacro category-of-idiots [[op & rest]]
  (conj rest
        (case op
          + (fn [x & _] (- x 123))
          - (fn [x & _] (+ x 42))
          op)))

(defmacro infixmaths [[x operator y]] (list operator x y))

; Macroexpand is really helpful for debugging macros
(macroexpand `(category-of-idiots (+ 10 4)))
(macroexpand `(infixmaths (1 + 2)))
(macroexpand `(when true "butts"))
