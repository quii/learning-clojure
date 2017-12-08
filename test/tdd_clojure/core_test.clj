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

(deftest including-files-namespaces-etc "This test is testing a function defined in src which further refers to an internal library, check the codes to see how. Remember in emacs with cider it's M+. to jump to symbol"
  (is (= 6 (a-cool-library-function 1))))

; Functions

(deftest functions "Calling functions is done by making a list where the first argument is the function you want to call. This is more consistent than non-lisps"
  (is (= false (= "BUM" "bum"))))

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

(deftest destructuring "This is sort of like pattern matching but obviously only positional based because dynamicness. Apparently it might be worth checking out core.match. This is preferable to OO style of creating classes to represent data, simply use maps and vectors to create these same abstractions in a more fluid (albeit less safe) way"
  (let [
        destruction-vector (fn [[first-name last-name & others :as full-list]] (str last-name " " first-name))
        destruction-map (fn [{name :first-name :or {name "Anonymous"}}] (str "Hello, " name))
        assoc-destructure (fn [{first-thing 0 third-thing 2}] [first-thing third-thing])
        ]
    (is (= "James Chris" (destruction-vector ["Chris" "James"])))
    (is (= "Hello, Chris" (destruction-map {:last-name "James" :first-name "Chris" :age 31})))
    (is (= "Hello, Anonymous" (destruction-map {:foo :bar})))
    (is (= ["One" "Three"] (assoc-destructure ["One" "Two" "Three"])))))

(deftest more-map-destructuring "Pick the keys you like"
  (let [pick-keys (fn [{:keys [a b]}] (+ a b))]
    (is (= 4 (pick-keys {:a 2 :b 2 :c 5})))))

(deftest map-destructure-keep-old-ones-too ":as lets you retain the original (this works with list destructuring too)"
                                           (let [pick-keys (fn [{:keys [a b] :as stuff}] stuff)]
                                             (is (= {:a 1 :b 2 :c 3} (pick-keys {:a 1 :b 2 :c 3})))))

(deftest map-destructure-defaults "you can set defaults with :or"
                                  (let [pick-keys (fn [{:keys [a b] :or {b 10}}] (+ a b))]
                                    (is (= 15 (pick-keys {:a 10 :b 5})))
                                    (is (= 15 (pick-keys {:a 5})))))

(deftest function-arrity "Functions can have multiple arity"
  (is (= "One argument" (arrity "Hi")))
  (is (= "Two arguments" (arrity "Hi" :chris)))
  )

(deftest composition "Comp takes a function which takes variable args and applies it to the right most function first"
    (is (= "16" ((comp str +) 8 8))))

(deftest rearranging "->> Allows you to rearrange your functions so that the first form is threaded through as the 3rd item (converted into a list if it isnt aready) into the first form, the result of which is passed to the 2nd form, etc. On the other hand -> sets it at the 2nd item "
  (is (= "234"
         (->> `(1 2 3) (map inc) (apply str))))
  (is (= 5
         (-> 4 (+ 1))))
  (is (= 31
         (let [data {:nested {:age 31 :eye-color "Blue"}}]
           (-> data :nested :age))))
  (is (= 10 (-> 5 add3 add2)))
)

; Collections
(deftest using-apply "Apply 'splats' a sequence of elements into a set of arguments to a function. Not like map which applies a function to each element in a collection to return a new collection"
  (is (= 6 (apply + [1 2 3]))))

(deftest vectors "Indexable, grow at the end, indexed by number, immutable, persistent. Vectors cannot be added to any faster than 0(n). Vectors are efficient at adding/removing from the end, accessing/changing by index and walking in reverse order. Vectors are bad for inserting things in between, use a hashmap or sortedmap instead. Dont use as a queue because rest and next return seqs not vectors, to convert them back again will give you a performance penalty. Use PersistentQueue instead, funnily enough."
  (is (= [10 20] (rest [0 10 20])))
  (is (= 10 (first [10 2 3])))
  (is (= 10 (second [2 10 3])))
  (is (= 10 (last [1 2 10])))
  (is (= 10 (nth [1 2 10] 2)))
  (is (= [:a :b :d] (assoc [:a :b :c] 2 :d )) "assoc only works with indexes that exist..")
  (is (= [:a :b :c :d] (assoc [:a :b :c] 3 :d)) "except if it is one index higher than the list, in which case it adds it to the end")
  (is (= [2 3 4] (next [1 2 3 4])))
  (is (= [1 2 4] (let [[x y _ z] [1 2 3 4 5]] [x y z])))
  (is (= [1 2 3] (vec (range 1 4))))
  (is (= [1 2 3] (into [1] [2 3])))
  (is (= [1 2 3] (conj [1 2] 3)) "This is synonymous with push on a stack")
  (is (= [1 2] (pop [1 2 3])) "And pop is pop, derp")
  (is (= 3 (peek [1 2 3])) "This is like 'last' but slower and not consistent with stack terminology, so use this! (last walks down the collection)")
  (is (= [1 2 3] (subvec (vec (range 1 10)) 0 3)))
  (is (vector? (first {:a "foo" :b "baz"})) "Elements in maps are vectors (derived, actual type is MapEntry")
  (is (= "foo" (val (first {:a "foo"}))) "key & val are convienience functions for nth 0 and 1")
  )

(deftest lists "Singly linked, grow at the front. Each node knows it's distance from the end. Elements can only be found by traversing from the start and you can only add/remove from the start. Usually you will prefer vectors as they're generally only used as Clojure code (like all the forms you see here!)"
  (is (= `(3 1 2) (conj `(1 2) 3)))
  (is (= `(4 3 1 2) (conj `(1 2) 3 4)))
  (is (= `(3 1 2) (cons 3 `(1 2))) "Cons and conj add to the start of a list. Conj is the right (efficient) way")
  (is (= 2 (second `(1 2 3))))
  (is (= 3 (nth `(1 2 3) 2))))

(deftest sets "All the collection things have constructor and macro versions to make them. You can use the set constructor which takes a vector"
  (let [some-set #{:a :b :c}]
    (is (= some-set #{:a :b :c}))
    (is (= nil ((set [1 2 3]) 4)))
    (is (= :a (some-set :a)))
    (is (= :not-found (get some-set :z :not-found)) "Advantage of using get is you can provide a default")
    ))

(deftest into-collections "into adds collections together"
  (is (= [1 2 3 4] (into [1 2] [3 4])))
  (is (= {:a "x" :b "y"} (into {:a "x"} {:b "y"}))))

(deftest range-to-create "Range creates things"
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
  (is (= false ((complement even?) 2)) "completement takes a truthy function and returns the opposite version")
  (is (= `("Ed") (filter #(= "Ed" %) `("Chris" "Ed" "Rob"))))
  (is (= 10 (reduce + [1 2 3 4])))
  (is (= [1 2 3 4] (concat [1 2] [3 4]))))

(deftest sort-stuff "sort hurrah"
(is (= [1 2 3] (sort [3 1 2])))
(is (= ["c" "bb" "aaa"] (sort-by count ["aaa" "c" "bb"])))
)

(deftest partial-fun "Partial takes a function and args and returns you a new function, it's worth bearing in mind that it applies the function to all arguments it gets"
  (let [add-5-to-things (partial + 5)]
    (is (= 305 (add-5-to-things 100 200)))
    (is (= 15 (add-5-to-things 10)))
    (is (= 15 (#(apply + 5 %&) 10)) "See how this is the equivalent of partial")))

(deftest more-partial-fun "Using other techniques to use functions in different ways. Apply (see butt-adder) lets you pass values as multiple args"
  (is (= "butt stinky" (butt-adder " stinky")))
  (is (= "butt stinky hole" (butt-adder [" stinky" " hole"])))
)

(deftest complement-fun "Complement negates a boolean"
  (let [my-filter #(> 5 %) my-collection (range 10 )]
    (is (= [0 1 2 3 4] (filter my-filter my-collection)))
    (is (= [5 6 7 8 9] (filter (complement my-filter) my-collection)))
))

(deftest map-collections "Map works how you'd expect, but it can take multiple collections or even multiple functions. When you do multiple collections the mapping function needs to be able to accept N number of arguments where N is number of collections. Hickey says they scale hugely. Map works with all collection types and returns a lazy **list**. The test seems to imply differently, but that's what clojure for the brave says? :S"
  (let [
        add2 (fn [x] (+ 2 x))
        print-me (fn [x] (do (println (str "Hello, " x)) x))
        add3 (fn [x] (+ 3 x))]
    (is (= [0 1] (take 2 (map print-me [0 1 2 3 4 5]))))
    (is (= [3 4 5] (map add2 [1 2 3])))
    (is (= [3 4] (map #(% 1) [add2 add3])))
    (is (= [3 5] (map + [1 1] [2 4])))
    (is (= [3 5] (map + [1 1] [2 4 5 6 7 7 8])))
    ))

(deftest more-map-quirks "Map works by calling it's arguments with 'seq' to turn them into a sequence. This means you dont get a map back for instance if you do map. Clojure handily has an identity function (like CT yeah!) which just returns what you give in, which helps you prove this. You can use 'into' to put it back into the data-structure you want"
  (let [my-map {:bums :willies}]
    (is (= (seq my-map) (map identity my-map)))
    (is (= my-map (into {} (map identity my-map))))))

(deftest replacing-elements "Clojure collections are persistent, so you will get new collections when you do these things. Obviously it doesnt just take a big copy, it's fast, efficient and wizzy."
  (let [my-collection [:1 :2 :3]]
    (is (= [:1 :2 :4] (replace {:3 :4} my-collection)))))


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
        nilly-map {:a nil}
        my-func (fn [{name :a}] name)]
    (is (= "Chris" (my-map :a)))
    (is (= "Chris" (:a my-map)))
    (is (= "A default value" (my-map-2 :wont-be-found "A default value")))
    (is (= "Chris" (my-func my-map)))
    (is (= true (contains? nilly-map :a)) "Note contains doesnt care that the key holds a nil value")))

(deftest updating-maps "Update-in"
  (let [x {:name "Chris" :age 31}]
    (is (= {:name "Chris" :age 32} (update-in x [:age] inc)))
    (is (= {:name "Chris" :age 33} (assoc x :age 33)))))

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
(deftest recursion "There's lots of interesting recursion stuff in clojure, i need to study it more. You should use recur instead of calling the function itself for the recursive call as that will make it tail-recursive (if its in the tail position"
  (is (= [5 4 3 2 1] (loop [result [] x 5]
              (if (zero? x) result
                  (recur (conj result x) (dec x)))))))

(deftest recur-with-when "When is like if but when you dont want an else. It also acts like a do (only last form returned). This is helpful for some recursive stuff "
  (let [count-down (fn [x]
                     (when (pos? x)
                       (println (str "COUNT DOWN - " x))
                       (recur (dec x))))
        ]
    (is (nil? (count-down 3)))))

(deftest quoting-in-depth "When creating a list you will have seen `(1 2 3) to prevent the form being evalauted (1 is not a function). You might want to have *some* of a quoted form evaluated though, ~ allows you to unquote"
  (is (= `(1 2 4) `(1 2 ~(+ 2 2)))))

(deftest truthiness "In clojure every value is true unless it is false or nil. This means empty collections are true. The seq function takes a collection and will return nil if it is empty'"
  (is (= true (when [] true)))
  (is (nil? (seq [])))
  (is (= 6 (recursive-with-seq [1 2 3]))))

(deftest regex "Regex is easy enough.."
  (is (= "ABC" (apply str (re-seq #"[A-Z]+" "bA1B3Ce ")))))

(deftest my-sum "Some function i made"
(is (= 6 (sum [1 2 3]))))
; Macros is a neat feature of lisps and is enabled by the very uniform syntax and treating "code as data". A lot of the standard lib is built using macros

(deftest reduce-fun "In this example reduce treets a map as a sequence of vectors ([k1 v1] [k2, v2] and applies the function creating a new map (the empty map argument"
  (is (= {:max 21 :min 10} (reduce map-incrementer {} {:max 20 :min 9})))
)

(deftest the-reader "The reader, reads your code and puts it into a list for evaluating"
  (let [my-form "(+ 1 2)"]
    (is (= '(+ 1 2) (read-string my-form)))
    (is (= 3 (eval (read-string my-form))))
))

(deftest my-infix-macro "The macro 'infix' lets you do maths like so"
  (is (= 5 (infix (3 + 2))))
)

; Macros have to return a list
(defmacro stupid-maths [[op & rest]]
  (conj rest
        (case op
          + -
          - +
          op)))


; Macroexpand is really helpful for debugging macros
(macroexpand `(when true "butts"))

; If you're using CIDER you can just use `C-c C-m`
(when true "butts")

