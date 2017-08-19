(ns entities.core
  "A proof of concept of an entity component system."
  (:gen-class)
  (:require [clojure.string :as str]
            [entities.components :as cmp]))

(def all-entities
  "All entities which are in the system."
  (ref '()))

(defn entity-name-valid?
  "Checks whether an entity name is valid.
  Returns a map with keys :result (true or false if failure)
  and :reason (nil if :result is true, string with message
  otherwise)."
  [entities name]
  (if (or (not (string? name))
          (str/blank? name))
    {:result false :reason "Name of the entity should be a non-empty string."}

    (if (some #(= name %) entities)
      {:result false :reason "Name of the entity should be unique."}
      {:result true})))


(defn create-entity
  "Creates a named entity. The name should be a unique string.
  If entity creates successfully, returns a map with :result true 
  :response {:entity newly created entity :entities all entities with the new one conjoined;
  otherwise :result false :response nil :reason string - the reason why it failed."
  [entities name]
  (let [name-valid (entity-name-valid? entities name)]
    (if-not (:result name-valid)
      {:result false :reason (:reason name-valid)}
      (let [entity {:name name :components '()}
            ents (conj entities entity)]
        {:result true :response {:entity entity :entities ents}}))))

(defn add-component
  "Adds a component to the entity. Returns a new entity with added component."
  [component entity]
  (update entity :components #(conj % component)))

(defn has-component?
  "Checks whether the entity has a particular component.
  comp-test is a function which takes one argument, a component, and
  returns true when the component is the required one, false otherwise."
  [comp-test entity]
  (some comp-test (:components entity)))

;; when replacing component, use hashmap as smap
;; e.g.  (replace {mc mc2} (:components ent))

