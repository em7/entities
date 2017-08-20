(ns entities.systems-test
  (:require [clojure.test :refer :all]
            [entities.systems :refer :all]
            [entities.components :as cmp]))

(deftest move-calc-coords-test
  (testing "Moved to correct position."
    (let [mc1 (:response (cmp/moveable-create [1 1 1] 5.0))
          mc2 (:response (cmp/moveable-create [0 0 0] 1.0))
          mm1 (move-calc-coords [1.0 1.0 1.0] mc1)
          mm2 (move-calc-coords [10 0.0 5] mc2)]
      (is (= [6.0 6.0 6.0] mm1))
      (is (= [10.0 0.0 5.0] mm2)))))
