(ns tdd_clojure.schema
  (:require [schema.core :as s]))

(defrecord Recipe
  [name
   description
   ingredients
   steps
   servings
   ])

(defrecord Ingredient
  [name
   quantity
   unit
   ])

(s/defrecord Ingredient
  [name :- s/Str
   quantity :- s/Int
   unit :- s/Keyword])

(s/defrecord Recipe
  [name :- s/Str
   description :- s/Str
   ingredients :- [Ingredient]
   steps :- [s/Str]
   servings :- s/Int])

(defrecord Store [,,,])

(defn cost-of [store ingredient] ,,,)

(defmulti cost (fn [entity store] (class entity)))

(defmethod cost Recipe [recipe store] 100)
(defmethod cost Ingredient [ingredient store] 50)

;; check with (s/check Recipe lasagna)
(def lasagna (map->Recipe
               {:name "Lasagna"
                :description "Lovely lovely lasagna"
                :ingredients [(->Ingredient "lasagna" 12 :sheet)
                              (->Ingredient "bol" 12 :spoons)
                              (->Ingredient "cheese" 123423 :grams)]
                :steps ["Put it"]
                :servings 6}))

;; what about invalid recipies?
(def mousaka (map->Recipe
               {:name "Mousaka"
                :steps ["Throw it in the oven"]
                :servings 6}))