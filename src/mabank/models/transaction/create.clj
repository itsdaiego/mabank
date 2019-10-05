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
  (let [params (:json-params req)]
    (hash-map :amount (:amount params)
              :installments (:installments params)
              :recipient-id (:recipient-id params))))

(defn save
  [req-params]
  (let [tx-result @(d/transact db/conn [{:db/id new-id
                                         :transaction/amount (:amount req-params)
                                         :transaction/installments (:installments req-params)
                                         :transaction/recipient (:recipient-id req-params)
                                         :transaction/status default-status
                                         }])]
    (assoc req-params  :transaction-id (d/resolve-tempid (:db-after tx-result) 
                                                         (:tempids tx-result) new-id))))
(defn build
  [req-params]
  (-> (hash-map :amount (:amount req-params)
                :installments (:installments req-params)
                :status default-status
                :recipient-id (:recipient-id req-params)
                :transaction-id (:transaction-id req-params))))

(defn run
  [req]
  (-> (parse-to-hashmap req)
      (save)
      (build)))

(hooke/add-hook #'build #'payable-create/run)
