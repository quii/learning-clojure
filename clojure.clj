(comment
Clojure notes. Gathered from links below and "Programming Clojure"
)

;; Strings

(str "Hello, " "world" "!")

;; Calling functions is uniform compared to other languages with the list syntax, first argument is the function and the rest are arguments)

(= "boo", "bum")
(apply + [1,2,3])

;; Contrast this to other languages. Why is concat("foo", "bar") and 1 + 2 acceptable when you think about it?

;; Creating functions

((fn add-five [x] (+ x 5)) 3) ;; can only use add-five in the context of the block, i think? (need to figure this out)
((fn [x] (+ x 5)) 3) ;; you dont have to give a name to the function

(#(+ % 5) 3) ;; sugar for above
(#(+ %1 %2), 2 5 )

((partial + 5) 3)

;; Creating documented functions that you can call

(defn greeting "Greets you" [name] (str "Hello, " name))
(doc greeting)
(greeting "Chris")

;; Composing functions

(defn x [a] (+ a 1))
(defn y [a] (+ a 2))

(-> 5 x y)
;; 8
(defn z [a] (-> a x y))

;; Collections
;; Lists

(list 1 2)
`(1 2)

;; Sequences

[20 30 40]
(seq [10, 20])
(= [1 2] (rest [0 1 2])) ;; for scala people, rest = tail
(first [10 20 30])
(second [10 20 30])
(last [10 20 30])

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

(conj {:chris 31 :ruth 30} [:stu 28] [:bob 70]) ;; conj also adds to maps


;; some collection functions

(map #(str "Hello, " % "!") `("Chris" "Ed" "Rob"))
(map (partial + 2) [1 2 3])
(mapcat #(list % %) `(1 2 3)) ;; flatmap - notice you need the "constructor" version of the collection creation
(filter #(= "Ed" %) `("Chris" "Ed" "Rob")) ;; returns Ed

;; records

(defrecord Person [first-name last-name])

; def evaluated only once whereas defn from earlier is always evaluated 
; this structure is also immutable
(def x (->Person "Chris" "James"))
(def x (Person. "Chris" "James"))

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
