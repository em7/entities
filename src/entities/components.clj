(ns entities.components
  (:gen-class))

(defn create-component
  "Creates a component which an entity can be composed of. Name of the component
  should be unique (is not validated in this function). State is an arbitrary value
  to be used by component functions."
  [name state]
  {:name name :state state})

(defn component?
  "Checks whether parameter is a component."
  [par]
  (and (map? par)
       (not (nil? (:name par)))))

(def moveable-name ::moveable)

(defn moveable-create
  "Creates a component which allows the entity to move on its own. Such entity
  should not be a part of the map itself, however it knows its position itself."
  [coords]
  (create-component moveable-name {::coords coords}))

(defn moveable?
  "Checks whether the component is a Moveable."
  [comp]
  (and (component? comp)
       (not (nil? (:name comp)))
       (= moveable-name (:name comp))))

(defn moveable-coords
  "Returns the coordinates from a moveable component."
  [comp]
  (::coords (:state comp)))

(defn moveable-move-to
  "Sets new coordinates to state of moveable component. Returns an updated entity."
  [comp new-coords]
  (assoc-in comp [:state ::coords] new-coords))

