(ns tdd_clojure.multimethods)

(defmulti shout :animal)

(defmethod shout :pig [_] "oink")
(defmethod shout :cat [_] "hisss")
(defmethod shout :dog [_] "bark")

(defmethod shout :default [_] "wtf")


;; rock paper scissors fun

;; express rules nicely!

(def hands [:paper :rock :scissors])

(defmulti beats? vector)
(defmethod beats? [:paper :rock] [_ _] true)
(defmethod beats? [:scissors :paper] [_ _] true)
(defmethod beats? [:rock :scissors] [_ _] true)
(defmethod beats? :default [_ _] false)

(defn draw [] (first (shuffle hands)))

(defn print-game [[player cpu]]
  (println (clojure.string/join " " ["You did" player "cpu did" cpu])))

(defn play []
  (let [player-hand (draw) cpu-hand (draw)]
    (do
      (print-game [player-hand cpu-hand])
      (beats? player-hand cpu-hand))))

;; multimethods with functions

(defmulti my-add (fn [a b] [(type a) (type b)]))            ;; dont quite get the syntax here..
(defmethod my-add [Integer Integer] ([a b] (+ a b)))
(defmethod my-add [String String] ([a b] (str a b)))