(ns entities.core
  "A proof of concept of an entity component system."
  (:gen-class)
  (:require [clojure.string :as str]
            [entities.components :as cmp]))



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
  :response newly created entity
  otherwise :result false :response nil :reason string - the reason why it failed."
  [entities name]
  (let [name-valid (entity-name-valid? entities name)]
    (if-not (:result name-valid)
      {:result false :reason (:reason name-valid)}
      (let [entity {:name name :components '()}
            ents (conj entities entity)]
        {:result true :response entity}))))



;; is adding a component to an entity really correct?



;; when replacing component, use hashmap as smap
;; e.g.  (replace {mc mc2} (:components ent))

