(ns mabank.logic.recipient-test
  (:require [mabank.logic.recipient :refer [validate-recipient]]
            [clojure.test :refer :all]))

(deftest recipient-payload-validation
  (testing "validate recipent payload"
    (is (= (validate-recipient {:name "Cameron Howe" :document-number "123456"}) []))))

(deftest recipient-payload-validation-without-name
  (testing "validate recipent payload without name"
    (is (= (validate-recipient {:document-number "123456"}) ["name is required!"]))))

(deftest recipient-payload-validation-without-document-number
  (testing "validate recipent payload without document number"
    (is (= (validate-recipient {:name "Cameron Howe"}) ["document number is required!"]))))

(deftest recipient-payload-validation-with-invalid-name
  (testing "validate recipent payload with invalid name"
    (is (= (validate-recipient {:name 42 :document-number "123"}) ["name must be a string"]))))

(deftest recipient-payload-validation-with-invalid-document-number
  (testing "validate recipent payload with invalid document-number"
    (is (= (validate-recipient {:name "Gordon Clark" :document-number 42}) ["document number must be a string"]))))
