(ns mabank.controllers.recipient
  (:require [mabank.db.recipients :as db]
            [mabank.adapter :as adapter]
            [mabank.logic.recipient :refer [validate-recipient]]))


(defn create-recipient!
  [storage payload]
  (let [validation (validate-recipient payload)]
    (if (= validation [])
      (-> (db/create-recipient! (:db storage) payload)
          (adapter/hashmap->json))

      (let [errors (adapter/vector-errors->json-errors validation)]
          (throw (ex-info "Validation Error"
                          {:type    "validation_error"
                           :cause errors}))))))

(defn fetch-recipients
  [storage]
  (-> (db/fetch-recipients (:db storage))
      (adapter/vector->json)))
