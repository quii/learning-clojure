(ns tdd_clojure.protocols)

(defprotocol my-protocol
  (shout [_]))

(defrecord Animal [name call]
  my-protocol (shout [_] (str call "!!!")))

(extend-protocol my-protocol java.lang.String
  (shout [this] (clojure.string/upper-case this)))