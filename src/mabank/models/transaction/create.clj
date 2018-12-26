(ns mabank.models.transaction.create
  (:require [mabank.db :as db]
            [datomic.api :as d]
            [cheshire.core :refer :all]))

(defn parse-to-hashmap
  [req]
  (let [params (get req :json-params)]
    (hash-map :amount (get params :amount)
              :installments (get params :installments)
              :recipient (get params :recipient))))

(defn save
  [params]
  @(d/transact db/conn [{
                         :transaction/amount (get params :amount)
                         :transaction/installments (get params :installments)
                         :transaction/recipient (get params :recipient-id)
                         :transaction/status "waiting-payemnt"
                         }])
  params)

(defn build-response
  [transaction]
  (-> (hash-map :amount (get transaction :amount)
                :installments (get transaction :installments)
                :recipient (get transaction :recipient)
                :status (get transaction :status))
      (generate-string)))

(defn run
  [req]
  (-> (parse-to-hashmap req)
      (save)
      (build-response)))
