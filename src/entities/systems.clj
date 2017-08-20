(ns entities.systems
  (:require [entities.components :as cmp]))


(defn move-calc-coords
  "Calculates new coordinates for a moveable component based on
  direction vector and the speed of moveable. The new coordinates are
  not validated therefore can be outside of map or in the wall. If
  there are less components of the direction vector than in the
  moveable coordinates, 0.0 is assumed for unpassed components."
  [direction moveable]
  (let [old-coords (cmp/moveable-coords moveable)
        speed (cmp/moveable-speed moveable)]
    (loop [o  (first old-coords)         ;first comp. of the old coordinates
           os (rest old-coords)          ;rest of the old coordinates
           d  (or (first direction) 0.0) ;direction delta of coords comp.
           ds (rest direction)           ;rest of the deltas
           n  []]                        ;accumulator for the new coordinates
      (if (nil? o)
        n
        (let [nn (+ o (* d speed))]
          (recur (first os)
                 (rest os)
                 (first ds)
                 (rest ds)
                 (conj n nn)))))))

(defn move
  "If component is moveable, creates a new moveable component with updated
  coordinates. Direction is a movement vector. The vector should have the same
  number of components as the component coordinates vector. New position is
  calculated as follows:
  
  x1 = x1 + (d1 * v)
  x2 = x2 + (d2 * v)
  where x1 is next/previous coordinate, d1 is component of a direction vector
  and v is the velocity.

  The calculated coordinates and the previous ones are passed to the bound-f which
  should be a (fn [previous-coordinates next-coordinates] ...) returning next coordinates
  possibly changed to be valid. E.g. if previous coordinates are 2 distance units left of wall
  and the entity needs to move right where the speed is 3, the next-coordinates are in the wall.
  The bound-f should return coordinates 1 square left of wall which is the maximum distance
  the entity can move because of the wall."
  [direction bound-f component]
  {})

;; (loop [o (first [1 2 3])
;;                       os (rest [1 2 3])
;;                       d (first [1 1 1])
;;                       ds (rest [1 1 1])
;;                       n []]
;;                  (if (or (nil? o)
;;                          (nil? d))
;;                    n
;;                    (let [nn (+ o d)]
;;                      (recur (first os)
;;                             (rest os)
;;                             (first ds)
;;                             (rest ds)
;;                             (conj n nn)))))
;; [2 3 4]
