(ns entities.core-test
  (:require [clojure.test :refer :all]
            [clojure.string :refer [blank?]]
            [entities.core :refer :all]
            [entities.components :as cmp]))

(defmacro is-result-false-reason-non-blank
  "Ensures that :result is false and :reason are non blank in the map.
  If :result is not false, the res-false-msg is used. If :reason is blank
  the reason-blank-msg is used.
  This macro should be used only within deftest.

  Expands to
  (do (is (= false (:result ~map)) ~res-false-msg)
      (is (not (blank? (:reason ~map))) ~reason-blank-msg))"
  [map res-false-msg reason-blank-msg]
  `(do (is (= false (:result ~map)) ~res-false-msg)
       (is (not (blank? (:reason ~map))) ~reason-blank-msg)))

(deftest entity-name-valid?-test
  (testing "Valid string if all-entities is an empty list."
    (is (= true (:result (entity-name-valid? '() "abc")))))
  (testing "Valid unique string if all-entities is a non-empty list."
    (is (= true (:result (entity-name-valid?
                          '("abc" "def" "ghi")
                          "jkl")))))
  (testing "Non-unique string is not a valid entity name."
    (let [ret (entity-name-valid? '("abc" "def" "ghi") "def")]
      (is-result-false-reason-non-blank ret
                                        "Non-unique name is not valid."
                                        "Reason is empty when non-unique name is tried.")))
  (testing "Blank string is not valid entity name."
    (let [ret-nil (entity-name-valid? '() nil)
          ret-empty (entity-name-valid? '() "")
          ret-white (entity-name-valid? '() " ")]
      (is-result-false-reason-non-blank ret-nil
                                        "nil is not a valid name"
                                        "Reason is empty when nil name is tried.")
      (is-result-false-reason-non-blank ret-empty
                                        "empty string is not a valid name"
                                        "Reason is empty when empty name is tried.")
      (is-result-false-reason-non-blank ret-white
                                        "whitespace string is not a valid name"
                                        "Reason is empty when whitespace name is tried.")))
  (testing "Non-string name is not allowed."
    (let [res (entity-name-valid? '() 5)]
      (is-result-false-reason-non-blank res
                                        "Non-string value is not a valid name."
                                        "Reason is empty when non-string name is tried."))))

(deftest create-entity-test
  (testing "Entity is added among other entities when name is valid."
    (let [ents (:response (create-entity '() "Test Osteron"))]
      (is (= 1 (count (:entities ents))) "A new entity should be added to empty collection.")
      (is (= "Test Osteron" (:name (first (:entities ents)))) "New entity among other entities should be named correctly.")
      (is (= "Test Osteron" (:name (:entity ents))) "New entity should be named correctly."))
    (let [ents (:response (create-entity '({:name "ent1"} {:name "ent2"}) "ent3"))]
      (is (= 3 (count (:entities ents))) "A new entity should be added to non-empty collection.")
      (is (some #(= "ent3" (:name %)) (:entities ents)) "A new entity should be named correctly."))))


(deftest add-component-test
  (testing "A component is added to an entity."
    (let [ent (:entity (:response (create-entity '() "entity")))
          ent2 (add-component (cmp/moveable-create [5 6]) ent)
          ent3 (add-component (cmp/create-component "MyFakeComponent" nil) ent2)]
      (is (= 1 (count (:components ent2))) "Number of comonents should be 1 when 1 component is added.")
      (is (true? (cmp/moveable? (first (:components ent2)))) "A correct component should be added.")
      (is (= 2 (count (:components ent3))) "When a new component is added, number of components should increase."))))

(deftest has-component?-test
  (testing "A moveable component should be in the entitiy if it was added."
    (let [ent (->> (:entity (:response (create-entity '() "entity")))
                   (add-component (cmp/moveable-create [10 50]))
                   (add-component (cmp/create-component "FakeComponent" nil)))
          has-moveable (has-component? cmp/moveable? ent)]
      (is (true? has-moveable)))))




