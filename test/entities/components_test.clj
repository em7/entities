(ns entities.components-test
  (:require [clojure.test :refer :all]
            [entities.components :refer :all]))

(deftest create-component-test
  (testing "Creating a new empty component."
    (let [ec (create-component "empty-name" "empty-state")]
      (is (= "empty-name" (:name ec)) "Component created with correct name.")
      (is (= "empty-state" (:state ec))) "Component created with correct state.")))

(deftest component?-test
  (testing "Map with key :name is a component."
    (is (true? (component? {:name "abc"}))))
  (testing "If parameter is not map with :name key, it's not component."
    (is (false? (component? "a string")) "A string should not be a component.")
    (is (false? (component? {})) "A map without :name key should not be a component.")))

(deftest moveable-create-test
  (testing "Creating a new moveable component."
    (let [mc (:response (moveable-create [5 6]))
          [x y] (:entities.components/coords (:state mc))]
      (is (and (= 5 x) (= 6 y)) "Moveable component created with correct coordinates.")
      (is (= moveable-name (:name mc)) "Moveable component created with correct name.")))
  (testing "Setting speed to a moveable component."
    (let [mc1 (:response (moveable-create []))
          mc2 (:response (moveable-create [] 5.9))]
      (is (= 1.0 (:entities.components/speed (:state mc1))) "If created without setting speed, 1.0 should be assumed.")
      (is (= 5.9 (:entities.components/speed (:state mc2))) "If created with 5.9 speed, the speed should be 5.9.")))
  (testing "If moveable created with wrong data types, false :result should be returned."
    (let [mr1 (:result (moveable-create nil 1.5))
          mr2 (:result (moveable-create [] "a"))]
      (is (false? mr1) "Creating moveable with non-vector coords should result in falsey :result.")
      (is (false? mr2) "Creating moveable with non-float speed should result in falsey :result."))))

(deftest moveable?-test
  (testing "Moveable should be a component."
    (is (false? (moveable? {})) "An empty map is not component and should not be moveable."))
  (testing "Moveable component should have :name identical to moveable-name."
    (is (true? (moveable? {:name moveable-name})))))

(deftest moveable-coords-test
  (testing "Getting correct coordinates from moveable component."
    (let [mc (moveable-create [1 2 3])]
      (is (= [1 2 3] (moveable-coords mc)) "Got wrong coordinates from moveable component."))))

(deftest moveable-speed-test
  (testing "Getting correct speed from moveable component."
    (let [mc (moveable-create nil 5.5)]
      (is (= 5.5 (moveable-speed mc))))))

(deftest moveable-move-to-test
  (testing "New coordinates should be assigned to moveable."
    (let [mc (moveable-create [1 2 3])
          mm (moveable-move-to mc [4 5 6])
          new-coords (moveable-coords mm)]
      (is (= [4 5 6] new-coords) "Moveable should have been moved to correct "))))
