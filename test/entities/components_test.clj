(ns entities.components-test
  (:require [clojure.test :refer :all]
            [entities.components :refer :all]))

(deftest create-component-test
  (testing "Creating a new empty component."
    (let [ec (create-component "entity-name" "empty-name" "empty-state")]
      (is (= "entity-name" (:entity ec)) "Component should be created with correct entity name.")
      (is (= "empty-name" (:name ec)) "Component should be created with correct name.")
      (is (= "empty-state" (:state ec))) "Component should be created with correct state.")))

(deftest component?-test
  (testing "Map with keys :entity and :name is a component."
    (is (true? (component? {:entity "ent" :name "abc"}))))
  (testing "If parameter is not map with :entity and :name keys, it's not component."
    (is (false? (component? "a string")) "A string should not be a component.")
    (is (false? (component? {})) "A map without :name or :entity key should not be a component.")
    (is (false? (component? {:entity "a"})) "A map without :name key should not be a component.")
    (is (false? (component? {:name "a"})) "A map without :entity key should not be a component.")))

(deftest component-entity-test
  (testing "Should return correct entity identifier."
    (is (= "IDENT" (component-entity (create-component "IDENT" "c-n" {}))))))

(deftest moveable-create-test
  (testing "Creating a new moveable component."
    (let [mc (:response (moveable-create "ENT" [5 6]))
          [x y] (:coords (:state mc))
          ent-id (component-entity mc)]
      (is (and (= 5 x) (= 6 y)) "Moveable component created with correct coordinates.")
      (is (= :moveable (:name mc)) "Moveable component created with correct name.")
      (is (= "ENT" ent-id) "Moveable component associated with correct entity identifier.")))
  (testing "Setting speed to a moveable component."
    (let [mc1 (:response (moveable-create "ENT" []))
          mc2 (:response (moveable-create "ENT" [] 5.9))]
      (is (= 1.0 (:speed (:state mc1))) "If created without setting speed, 1.0 should be assumed.")
      (is (= 5.9 (:speed (:state mc2))) "If created with 5.9 speed, the speed should be 5.9.")))
  (testing "If moveable created with wrong data types, false :result should be returned."
    (let [mr1 (:result (moveable-create "ENT" nil 1.5))
          mr2 (:result (moveable-create "ENT" [] "a"))]
      (is (false? mr1) "Creating moveable with non-vector coords should result in falsey :result.")
      (is (false? mr2) "Creating moveable with non-float speed should result in falsey :result."))))

(deftest moveable?-test
  (testing "Moveable should be a component."
    (is (false? (moveable? {})) "An empty map is not component and should not be moveable."))
  (testing "Moveable component should have :name identical to :moveable and state should have :speed and :coords keys."
    (is (true? (moveable? {:entity "ENT" :name :moveable :state {:speed 1.0 :coords []}}))))
  (testing "If :state or its component is missing, map is not Moveable component."
    (is (false? (moveable? {:entity "ENT" :name :moveable})) "Should not be Moveable component if :state is completely missing.")
    (is (false? (moveable? {:entity "ENT" :name :moveable :state {}})) "Should not be Moveable component if :state is empty.")
    (is (false? (moveable? {:entity "ENT" :name :moveable :state {:speed 1.0}})) "Should not be Moveable component if :state has only :speed.")
    (is (false? (moveable? {:entity "ENT" :name :moveable :state {:coords []}})) "Should not be Moveable component if :state has only coords.")))

(deftest moveable-move-to-test
  (testing "New coordinates should be assigned to moveable."
    (let [mc (:response (moveable-create "ENT" [1 2 3]))
          mm (moveable-move-to mc [4 5 6])
          new-coords (moveable-coords mm)]
      (is (= [4 5 6] new-coords) "Moveable should have been moved to correct "))))


