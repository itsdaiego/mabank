(ns mabank.models.helper.transaction-test
  (:require [datomic.api :as d]))


(def new-id (d/tempid :db.part/user))

(defn create
  [conn]
  (let [tx-result @(d/transact conn [{:db/id new-id
                                         :transaction/amount 100
                                         :transaction/installments 1
                                         :transaction/recipient 123
                                         :transaction/status "waiting-payemnt"
                                         }])]
    (hash-map :transaction-id (d/resolve-tempid (:db-after tx-result) 
                                                         (:tempids tx-result) new-id))))
(defn fetch
  [conn]
  (d/q '[:find ?e ?amount ?installments ?recipient-id ?status
         :where [?e :transaction/amount _]
         [?e :transaction/amount ?amount]
         [?e :transaction/installments ?installments]
         [?e :transaction/recipient ?recipient-id]
         [?e :transaction/status ?status]]
       (d/db conn)))
