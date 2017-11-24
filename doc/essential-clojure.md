# Notes on essential clojure

## Modeling Entities

Either use maps or records to represent domain entities. 

- Maps are uh maps

### Records

- Records construct a type with predefined structure for well known fields
- Provide class-like features, well-known fields and constructors to support domain entitiies

### How to choose?
- Both map and record use standard map collection functions
- Most of the time records are better for domain entities as they leverage features of the JVM to provide better performance (creates a Java class)
- Records also provide more features
- Maps may be better fort public APIs to minimise constraints on callers (so they dont have to create instances of our specific things)


### Making code flexible

Spotting a general theme
- Making functions easy to use and hard to get wrong
- Use destructuring, multiple arity.
- Use maps for optional arguments and use `merge` with a default map combined with `let` to make it easy for both the caller and the function body


## Modelling Relationships

Entities can refer to each other via
- Nesting
- Identifiers
- Stateful references

The first 2 are emphasised in clojure

## Domain operations

- Often need to define f that can be applied to different types of domain entities, in OO achieved via polymorphism
- Generic domain operations in clojure are done with either *multimethods* or *protocols*


### Multimethods
`(defmulti cost (fn [entity store] (class entity)))` defines a multimethod, then you define instances
`(defmethod cost Recipe [recipe store] (some logic with store and recipe))`

### Protocols

```
;; protocols can define > 1 function
(defprotocol Cost
  (cost [entity store]))
  
;; you can extend functions in multiple forms if you want
(extend-protocol Cost
  Recipe     
  (cost [recipe store]
    (reduce +$ zero-dollars
      (map #(cost % store) (:ingredients recipe))))
  Ingredient 
  (cost [ingredient store]
    (cost-of store ingredient)))

```

- Protocols are faster than multimethods because they leverage JVM runtime optimisations for dispatch
- Protocols have ability to group related functions together in a single protocol
- protocols preferred for type-based dispatch
- Multimethods can prvide value based dispatch on any or all of the functions arguments (as opposed to just the first)

## Value based dispatch

- Rather than types look at value, this is where multimethods rule
- todo: make an example and write some tests