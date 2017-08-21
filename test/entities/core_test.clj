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
    (let [ent (:response (create-entity '() "Test Osteron"))]
      (is (= "Test Osteron" (:name ent)) "New entity should be named correctly if created separately."))))











