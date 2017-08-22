(ns entities.components
  (:gen-class))

(defn create-component
  "Creates a component which an entity can be composed of. Name of the
  component should be unique within the entity (is not validated in
  this function, i.e. the entity should not have this component
  already.). State is an arbitrary value to be used by component
  functions. Entity-name is an arbitrary value which identifies the
  entity."
  [entity-name name state]
  {:entity entity-name :name name :state state})

(defn component?
  "Checks whether parameter is a component."
  [par]
  (and (map? par)
       (not (nil? (:name par)))
       (not (nil? (:entity par)))))

(defn component-entity
  "Retunrns an entity identifier which this component belongs to. The
  entity identifier is the same value which was passed during its
  creations."
  [comp]
  (:entity comp))

(defn moveable-create
  "Creates a component which allows the entity to move on its
  own. Such entity should not be a part of the map itself, however it
  knows its position itself. Entity-name is an arbitrary value which
  identifies the entity. If speed is not provided, 1.0 is
  assumed. Coords should be a vector and speed float. If coords is
  not a vector and speed is not float, then map is returned
  with :result false, :response nil and :reason string reason. If OK
  then map is returned with :result true, :response component
  and :reason nil."
  ([entity-name coords]
   (moveable-create entity-name coords 1.0))
  ([entity-name coords speed]
   (if-not (vector? coords)
     {:result false :reason "Coordinates should be a vector."}
     (if-not (float? speed)
       {:result false :reason "Speed should be float."}
       {:result true
        :response (create-component entity-name
                                    :moveable
                                    {:coords coords :speed speed})}))))

(defn moveable?
  "Checks whether the component is a Moveable."
  [comp]
  (and (component? comp)
       (not (nil? (:name comp)))
       (= :moveable (:name comp))
       (not (nil? (get-in comp [:state :coords])))
       (not (nil? (get-in comp [:state :speed])))))

(defn moveable-coords
  "Returns coordinates from moveable component state."
  [moveable]
  (get-in moveable [:state :coords]))

(defn moveable-speed
  "Returns speed from moveable component state."
  [moveable]
  (get-in moveable [:state :speed]))

(defn moveable-move-to
  "Sets new coordinates to state of moveable component. Returns an updated component."
  [moveable new-coords]
  (assoc-in moveable [:state :coords] new-coords))




