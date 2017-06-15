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

(def sum #(reduce + %))

(def avg #(/ (sum %) (count %)))

(defn stats [numbers] (map #(% numbers) [sum count avg]))

(defn add-five [x] (+ x 5))

(defn greeting "Greets you" [name] (str "Hello, " name))
; try (doc greeting)

(defn arrity
  ([x] "One argument")
  ([x y] "Two arguments")
  ([x y & otherargs] "More than 2 arguments"))

(defn map-incrementer 
  [data [key val]]
  (assoc data key (inc val))
  )

(defn butt-adder [values] (apply (partial str "butt") values))

(require '[clojure.string :as s])
(defn clean 
  [text]
  (s/replace (s/trim text)  #"lol" "LOL")
)

(defn sleepy [x] (Thread/sleep 1000) x)
(def memo-sleepy 
  "Memoize will save function return values based on args"
  (memoize sleepy)
)
