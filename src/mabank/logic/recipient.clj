(ns mabank.logic.recipient
  (:require [bouncer.core :as b]
            [bouncer.validators :as v]))

(defn validate
  [payload]
  (b/validate payload
               :name [[v/required :message "name is required!"] [v/string :message "name must be a string"]]
               :document-number [[v/required :message "document number is required!"] [v/string :message "document number must be a string"]]))

(defn format-response
  [payload]
  (if (= (first payload) nil)
    [])
  (let [errors (:bouncer.core/errors (second payload))]
    (vec (map #(first (val %)) errors))))

(defn validate-recipient
  [payload]
  (-> (validate payload)
      (format-response)))
