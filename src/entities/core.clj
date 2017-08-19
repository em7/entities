(ns entities.core
  "A proof of concept of an entity component system."
  (:gen-class)
  (:require [clojure.string :as str :refer [blank?]]))

(def all-entities
  "All entities which are in the system."
  (ref '()))

(defn entity-name-valid?
  "Checks whether an entity name is valid.
  Returns a map with keys :result (true or false if failure)
  and :reason (nil if :result is true, string with message
  otherwise)."
  [all-entities name]
  (if (or (not (string? name))
          (str/blank? name))
    {:result false :reason "Name of the entity should be a non-empty string."}

    (if (some #(= name %) all-entities)
      {:result false :reason "Name of the entity should be unique."}
      {:result true :reason nil})))


;; (defn create-entity
;;   "Creates a named entity. The name should be a unique string."
;;   [name]
;;   (if (some #()))
;;   )


