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
  :response newly created entity otherwise :result false :response
  nil :reason string - the reason why it failed."
  [entities name]
  (let [name-valid (entity-name-valid? entities name)]
    (if-not (:result name-valid)
      {:result false :reason (:reason name-valid)}
      (let [entity {:name name :components '()}
            ents (conj entities entity)]
        {:result true :response entity}))))

(defn entity-name
  "Returns a name of the entity."
  [entity]
  (:name entity))



;; is adding a component to an entity really correct?

;; following methods should be composable. They get
;; a map with result of previous method as the last argument
;; either {:result true :response ... }
;; or     {:result false :reason list of strings - error messages}

(defn create-main-entity
  "If creating the main game entity succeeds, returns a map 
  
  {:result true :response entity}
  
  otherwise 
  
  {:result false :reason list of strings error messages}
  
  All-entities are not modified, just used for validation of entity
  name."
  [all-entities]
  (let [res (create-entity all-entities "main-entity")]
    (if (:result res)
      {:result true :response (:response res)}
      {:result false :reason (:reason res)})))

(defn create-main-moveable
  "Creates a Moveable component for the main entity.
  Coords should be a vector of floats where the player begin. Speed is a
  movement speed, float type.  The last parameter should be a map,
  either

  {:result true :response main-entity}

  or {:result false :reason list of error messages}

  Response is map

  {:result true :response {:entity main-entity :moveable moveable component}}
  
  if created successfully. If not, :result is false and :reason is
  given. If :result of previous operation is false, nothing is done
  and the map is passed as result of this method.
  "
  [coords speed prev-res]
  (if (false? (:result prev-res))
    prev-res
    (let [main-entity (:response prev-res)
          mov-r (cmp/moveable-create (entity-name main-entity) coords speed)]
      (if (false? (:result mov-r))
        mov-r
        {:result true :response
         {:entity main-entity
          :moveable (:response mov-r)}}))))

(defn main
  "Just a test function which creates entity and moves it."
  []
  (let [ents (atom '())]
    (let [res (->> '()
                   (create-main-entity)
                   (create-main-moveable [0.0 5.0] 1.4))]
      (when (:result res)
        (let [main-entity (get-in res [:response :entity])
              moveable    (get-in res [:response :moveable])]
          (swap! ents #(conj % main-entity))
              (println (str "Number of entities: " (count @ ents)))
              (println (str "Moveable: " moveable))
              (time (loop [n 0
                           mov moveable]
                      (if (> n 5000)
                        (println (str "Final position: " (cmp/moveable-coords mov)))
                        (do
                          ;;(println (str "Position " n ": " (cmp/moveable-coords mov)))
                          (let [m-n (cmp/moveable-move-to mov [n n])]
                            (recur (+ 1 n) m-n)))))))))
    ))

(defn -main
  []
  (let [t (main)]
    (println (str "Elapsed time: " t))))

;; when replacing component, use hashmap as smap
;; e.g.  (replace {mc mc2} (:components ent))
