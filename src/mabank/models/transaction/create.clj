(ns mabank.models.transaction.create
  (:require [mabank.db :as db]
            [mabank.models.payable.create :as payable-create]
            [datomic.api :as d]
            [robert.hooke :as hooke]))

(def conn db/conn)

(def default-status "waiting-payemnt")

(def new-id (d/tempid :db.part/user))


(defn parse-to-hashmap
  [req]
  (let [params (get req :json-params)]
    (hash-map :amount (get params :amount)
              :installments (get params :installments)
              :recipient-id (get params :recipient-id))))

(defn save
  [req-params]
  (let [tx-result @(d/transact conn [{:db/id new-id
                                         :transaction/amount (get req-params :amount)
                                         :transaction/installments (get req-params :installments)
                                         :transaction/recipient (get req-params :recipient-id)
                                         :transaction/status default-status
                                         }])]
    (assoc req-params  :transaction-id (d/resolve-tempid (:db-after tx-result) 
                                                         (:tempids tx-result) new-id))))
(defn build
  [req-params]
  (-> (hash-map :amount (get req-params :amount)
                :installments (get req-params :installments)
                :status default-status
                :recipient-id (get req-params :recipient-id)
                :transaction-id (get req-params :transaction-id))))

(defn run
  [req]
  (-> (parse-to-hashmap req)
      (save)
      (build)))


(hooke/add-hook #'build #'payable-create/run)
