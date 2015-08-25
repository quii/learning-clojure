; records

(defrecord Person [first-name last-name])

; this structure is also immutable
(def x (->Person "Chris" "James"))
(def x (Person. "Chris" "James"))








