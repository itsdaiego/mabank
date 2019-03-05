(ns mabank.models.transaction.create
  (:require [mabank.db :as db]
            [mabank.models.payable.create :as payable-create]
            [datomic.api :as d]
            [robert.hooke :as hooke]))

(def default-status "waiting-payemnt")

(defn parse-to-hashmap
  [req]
  (let [params (get req :json-params)]
    (hash-map :amount (get params :amount)
              :installments (get params :installments)
              :recipient-id (get params :recipient-id))))

(defn save
  [params]
  @(d/transact db/conn [{
                         :transaction/amount (get params :amount)
                         :transaction/installments (get params :installments)
                         :transaction/recipient (get params :recipient-id)
                         :transaction/status default-status
                         }])
  params)

(defn build-response
  [transaction]
  (-> (hash-map :amount (get transaction :amount)
                :installments (get transaction :installments)
                :status default-status
                :recipient (get transaction :recipient-id))))

(defn run
  [req]
  (-> (parse-to-hashmap req)
      (save)
      (build-response)))


(hooke/add-hook #'build-response #'payable-create/run)
