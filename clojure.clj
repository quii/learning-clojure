(str "Hello, " "world" "!")

;; Calling functions is uniform compared to other languages with the list syntax, first argument is the function and the rest are arguments)

(= "boo", "bum")
(apply + [1,2,3])

;; Contrast this to other languages. Why is concat("foo", "bar") and 1 + 2 acceptable when you think about it?

;; Creating functions
(defn add-five [x] (+ x 5))
(add-five 5)

((fn add-five [x] (+ x 5)) 3) ;; can only use add-five in the context of this form
((fn [x] (+ x 5)) 3) ;; you dont have to give a name to the function

(#(+ % 5) 3) ;; sugar for above
(#(+ %1 %2), 2 5 )

((partial + 5) 3)

;; Creating documented functions
(defn greeting "Greets you" [name] (str "Hello, " name))
(doc greeting)
(greeting "Chris")

;; Arity
(defn harrity
  ([x] (println "One arg"))
  ([x y] (println "Two args"))
  ([x y z] (println "Three args"))
  ([x y z & otherargs] (println otherargs)))

;; Composing functions
(defn x [a] (+ a 1))
(defn y [a] (+ a 2))

(-> 5 x y)
;; 8
(defn z [a] (-> a x y))

;; Lists
(list 1 2)
`(1 2)

;; Sequences
[20 30 40]
(seq [10, 20])
(= [1 2] (rest [0 1 2])) ;; for scala people, rest = tail

(first [10 20 30])
(first {:name "Chris" :age 10}) ; returns age, interestingly

(second [10 20 30])
(last [10 20 30])

(cons 1 [2 3 4])
(cons {:new-field "Blah"} {:x "y"})

(next [1 2 3 4])

(sorted-set 50 2 10 1)

; these insert efficiently, for a list it will add to the beginning and for a vector the end
(conj [1 2] 3 4)
(into [1 2] [3 4])

;; Ranges

(range 10)
(range 5 10)
(range 0 10 3)

(take 2 [1 2 3 4 5 6 7 8 9])
(take 5 (iterate inc 1))

; When repeat is called with one argument it returns a lazy infinite sequence
(take 5 (repeat "Chris rules"))

; If you define a second arg then it doesnt
(repeat 5 10)

; Cycle takes a collection and cycles through it infinitely
(take 5 (cycle [1 2]))

(interleave [1 2 3] ["A" "B" "C"])

(interpose "," ["Chris" "Ruth" "Simon"])
(apply str (interpose ", " ["Chris", "Ruth"]))

;; some more collection functions

(drop-while #(< % 5) [3 4 5 6 7 8])
(every? #(< % 5) [1 2 3])
(some even? [2 4 6])
; see also not-every? not-any?


(map #(str "Hello, " % "!") `("Chris" "Ed" "Rob"))
(map (partial + 2) [1 2 3])

(mapcat #(list % %) `(1 2 3)) ;; flatmap - notice you need the "constructor" version of the collection creation

(filter #(= "Ed" %) `("Chris" "Ed" "Rob")) ;; returns Ed

(reduce + [1 2 3 4])

;; Vectors

[1 2 3]
(vector 1 2 3 4)

;; Sets 
(set `(1 2 3))
#{1 2 3}
(conj #{1 2} 3) ;;conj adds to a set

;; Hashmaps

(hash-map "a" 10, "b" 20)
(hash-map "a" 10 "b" 20) ;; dont need commas, but good for readability
(hash-map :chris 31 :ruth 30) ;; the : creates a symbol
{:chris 31 :ruth 30} ;; more sugar

({:a 10, :b 20, :c 30} :b) ;; 20
(get {:chris 31} :chris 123)
(get {:chris 31} :bob 123) ;; defaults to 123 when not found

; records

(defrecord Person [first-name last-name])

; def evaluated only once whereas defn from earlier is always evaluated 
; this structure is also immutable
(def x (->Person "Chris" "James"))
(def x (Person. "Chris" "James"))

; quoting is where you put a ` before a form.
(def failed-movie-titles ["Gone With the Moving Air" "Swellfellas"])
(def failed-languages ["Java", "PHP"])

; the first example is giving clojure the symbol and it returns the object
failed-movie-titles
; below is quoting the symbol as a data-type and then evaluating it
(eval `failed-movie-titles)
; (first `failed-movie-titles) would fail as it's no longer a collection
(first ['failed-movie-titles `failed-langiages]) ;will work

; State

; Clojure provides reference types (ref) for keeping track of state (??)

(def visitors (atom #{})) ; atoms are thread-safe

; To update a reference a function must be used like...

(swap! visitors conj "Chris")

(deref visitors) ; to have a look at our visitors
@visitors ; or you can use @

;let evaluates a function in a lexical context where the arguments are bound to values
(let [x 5 y 10] (+ x y))

; Let and destructuring

; In this example I am using let to define a map of data and a function which uses destructuring to
; make it so a function only gets the data it wants in a format it cares about.
; It does not care about last names, it only wants to take a first-name and call it name
(let [x {:first-name "Chris" :last-name "James"} y (fn [{name :first-name}] (println "Hello" name))] (y x))
; Hello Chris

; You can also destructure collections
(let [[x y _ z] [1 2 3 4 5]] [x y z])

; docs
(find-doc "ns-")

; java
(def rnd (new java.util.Random))

 ; obvious in a way, '.' is a function on class instances
(println (. rnd nextInt))

; static calls
(. Math PI)

; if 
(if true (println "YES!") (println "NO :("))

; it's worth noting that if is a "special form" because it doesn't evaulate all it's arguments recursively like normal forms (i.e we dont print NO in this case

; if you dont supply the else part (3rd arg) and the 1st arg resolves to false then it returns nil. You can check for nil with nil?
(nil? (if false 10))

; Side effects with do. do takes any number of forms and returns the last one
(if true (do (println "yes") 123))

; when is if without an else
(when true (println "Howdy"))

; this will return a function, just to show you can
((or + -) 2 2 2 )

; cond

(let [x (fn [x]
          (cond (= x 0) "is 0" (= x 1) "is 1" :else "is something else"))]
  (x 1))

; loop works like let, establishing bindings and then evaulating expressions

; Loop with an empty array and 5. If 5 is zero then return array otherwise
; recur calls the loop back with x added to result array and decrementing x
(loop [result [], x 5]
  (if (zero? x) result
      (recur (conj result x) (dec x))))

; However clojure has abstractions on collections you'd expect
(into [] (take 5 (iterate dec 5)))

; metadata

(defn ^{:tag String} shout [^{:tag String} s] (.toUpperCase s))
; (meta #`shout)


; Macros
(defn clock-add [& numbers] (mod (apply + numbers) 12))
(clock-add 10 4)

; Macros have to return a list
(defmacro category-of-clocks [[op & rest]]
  (conj rest `clock-add))

; Macroexpand is really helpful for debugging macros
(macroexpand `(category-of-clocks (+ 10 4)))
(category-of-clocks (+ 10 4))
