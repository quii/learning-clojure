(ns tdd_clojure.essential_scratch)

;; yay for records
(defrecord Planet [name moons])

;; Positional factory requires attributes to be sent in correct order which means more likely to be broken
(def earth (->Planet "Earth" 1))

;; Map factory is more verbose but less error prone. Also continues to work if new attributes are added
(def earth2 (map->Planet {:name "Earth" :moons 1}))


;; Positional destructuring is cool for optional arguments (try in repl)
;; If you're frequently using functions like first, second or nth consider destructuring
(defn make-thing [f1 f2 & [f3 f4]] {:a f1 :b f2 :c f3 :d f4})

;; Map destructuring, accept optional arguments in any order
;; todo: why doesnt this work? (make-thing2 1 2 {:f1 1 :f2 2 :f3 3 :f4 4})
(defn make-thing2 [f1 f2 & {:keys [f3 f4] :as opts}]
  (do (println opts) ({:a f1 :b f2 :c f3 :d f4})))


;; new fun

;; wtf is declare - defs the supplied var names with no bindings, useful for making forward declarations.
(declare validate-same-currency)

(defrecord Currency [divisor sym desc])

;; what does the hat mean?
;; im guessing this is making the record impl comparable, nifty
(defrecord Money [amount ^Currency currency]
  java.lang.Comparable
  (compareTo [m1 m2]
    (validate-same-currency m1 m2)
    (compare (:amount m1) (:amount m2)))
  )

(def currencies {:usd (->Currency 100 "USD" "US Dollars")
                 :eur (->Currency 100 "EUR" "Euro")})

(defn- validate-same-currency
  [m1 m2]
  (or (= (:currency m1) (:currency m2))
      (throw
        (ex-info "Currencies do not match."
                 {:m1 m1 :m2 m2}))))
(defn =$
  ([m1] true)
  ([m1 m2] (zero? (.compareTo m1 m2)))
  ([m1 m2 & monies]
   (every? zero? (map #(.compareTo m1 %) (conj monies m2)))))

(defn +$
  ([m1] m1)
  ([m1 m2]
   (validate-same-currency m1 m2)
   (->Money (+ (:amount m1) (:amount m2)) (:currency m1)))
  ([m1 m2 & monies]
   (reduce +$ m1 (conj monies m2))))
(defn *$ [m n] (->Money (* n (:amount m)) (:currency m)))

;; use (make-money 5 (:eur currencies)) to make 5 euro (remember map keys are functions)
(defn make-money
  ([] (make-money 0))
  ([amount] (make-money amount :usd))
  ([amount currency] (->Money amount currency)))

;; Constructor calculations
(defrecord Manuscript [title abstract impactFactor])

(defn make-manuscript "Creates manuscript with a computed impact factor" [title abstract]
  (->Manuscript title abstract (rand-int 100)))
