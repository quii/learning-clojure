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

(def this-is-mutable (ref 1))

(deftest state (testing  "Clojure is immutable by default but obviously it can be managed. The ref keyword makes a reference which can be mutated. @ or deref can get the value. To change it you use ref-set in conjunction with dosync which makes it a transaction to be thread safe"
                 (is (= 2 (dosync (ref-set this-is-mutable 2))))))

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

(deftest composition "Comp takes a function which takes variable args and applies it to the right most function first"
    (is (= "16" ((comp str +) 8 8))))


; Collections
(deftest using-apply "Apply 'splats' a sequence of elements into a set of arguments to a function. Not like map which applies a function to each element in a collection to return a new collection"
  (is (= 6 (apply + [1 2 3]))))

(deftest sequences "You can chop up sequences how you'd expect"
  (is (= [10 20] (rest [0 10 20])))
  (is (= 10 (first [10 2 3])))
  (is (= 10 (second [2 10 3])))
  (is (= 10 (last [1 2 10])))
  (is (= [2 3 4] (next [1 2 3 4])))
  (is (= [1 2 4] (let [[x y _ z] [1 2 3 4 5]] [x y z]))))

(deftest cons-sequences "Cons lets you stick things on the end of a collection" 
  (is (= [1 2 3] (cons 1 [2 3]))))

(deftest conj-collectons "Conj is like cons but adds to the start and returns a collection"
  (is (= `(3 1 2) (conj `(1 2) 3)))
  (is (= `(4 3 1 2) (conj `(1 2) 3 4))))

(deftest into-collections "into adds collections together"
  (is (= [1 2 3 4] (into [1 2] [3 4])))
  (is (= {:a "x" :b "y"} (into {:a "x"} {:b "y"}))))

(deftest range "Range creates things"
  (is (= [0 1 2 3 4 5] (clojure.core/range 6)))
  (is (= [1 2 3] (clojure.core/range 1 4)))
  (is (= [0 3 6 9] (clojure.core/range 0 10 3))))

(deftest take-and-repeat "Take allows you to take a number of elements from a collection, useful with lazy & infinite collections like 'repeat', 'cycle' and 'iterate'"
  (is (= [:x :x] (take 2 (repeat :x))))
  (is (= [1 2 3] (take 3 (iterate inc 1))))
  (is (= [1 2 1] (take 3 (cycle [1 2])))))

(deftest other-collection-goodies "For fun and profit, see also drop-while, not-every? and not-any?"
  (is (= `(1 2 3) (take-while #(< % 4) (iterate inc 1))))
  (is (= true (every? #(< % 5) [1 2 3])))
  (is (= true (some even? [1 2 3 10])))
  (is (= `("Ed") (filter #(= "Ed" %) `("Chris" "Ed" "Rob"))))
  (is (= 10 (reduce + [1 2 3 4]))))

(deftest map-collections "Map works how you'd expect, but it can take multiple collections or even multiple functions. When you do multiple collections the mapping function needs to be able to accept N number of arguments where N is number of collections"
  (let [
        add2 (fn [x] (+ 2 x))
        add3 (fn [x] (+ 3 x))]
    (is (= [3 4 5] (map add2 [1 2 3])))
    (is (= [3 4] (map #(% 1) [add2 add3])))
    (is (= [3 5] (map + [1 1] [2 4])))
    (is (= [3 5] (map + [1 1] [2 4 5 6 7 7 8])))
    ))

(deftest recursion-with-recur "Clojure only has one non-stack-consuming looping construct: recur"
  (let [my-func (loop [x 5
         result []]
    (if (> x 0)
      (recur (dec x) (conj result (+ 2 x)))
      result))]
    (is (= [7 6 5 4 3] my-func))))

(deftest hashmaps "Hashmaps are defined as key value list, you can use commas for readability. :symbol defines a symbol which is nice for keys, but they can just be strings. When writing functions you can destructure maps into the name you want and ignore fields you dont care about"
  (let [
        my-map (hash-map :a "Chris" :b "Ruth")
        my-map-2 {:a "Jim" :b "Rob"}
        my-func (fn [{name :a}] name)]
    (is (= "Chris" (my-map :a)))
    (is (= "A default value" (my-map-2 :wont-be-found "A default value")))
    (is (= "Chris" (my-func my-map)))))

; if's nils and things

(deftest side-effects "do returns the last form"
  (is (= 2 (do (println "Side effect") 2))))

(deftest ifff "if is a macro as it doesnt evaluate both sides, if you dont add an else then nil could be returned"
  (is (= nil (if false 10))))

(deftest calling-static-java "Calling java is done like so"
  (is (= (. Math PI) (. Math PI))))

(deftest cond-is-multiple-if "Dont confuse me with case"
  (let [x :woo]
    (is (= 10 (cond
               (= x :woo) 10
               (= x :boo) 20
               (= x :foo) 30
               :else "Some default"
               )))))

; Recursion
(deftest recursion "There's lots of interesting recursion stuff in clojure, i need to study it more. Here's one example that's not tail recursive, i think"
  (is (= [5 4 3 2 1] (loop [result [] x 5]
              (if (zero? x) result
                  (recur (conj result x) (dec x)))))))

; Macros is a neat feature of lisps and is enabled by the very uniform syntax and treating "code as data". A lot of the standard lib is built using macros

; Macros have to return a list
(defmacro stupid-maths [[op & rest]]
  (conj rest
        (case op
          + -
          - +
          op)))

(defmacro infix [[x operator y]] (list operator x y))

; Macroexpand is really helpful for debugging macros
(macroexpand `(when true "butts"))

; If you're using CIDER you can just use `C-c C-m`
(when true "butts")

