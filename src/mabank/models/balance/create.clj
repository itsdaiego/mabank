(ns mabank.models.balance.create
  (:require [mabank.db :as db]
            [datomic.api :as d]
            [cheshire.core :refer :all]))

(defn create
  [payable]
  @(d/transact db/conn [{:db/id (d/tempid :db.part/user)
                                         :balance/status "future-payment"
                                         :balance/amount (get payable :amount)
                                         :balance/fee (get payable :fee)
                                         :balance/recipient (get payable :recipient-id)
                                         :balance/payable (get payable :payable-id)
                                         }]))

(defn save
  [payables]
  (doall (map create payables)))

(defn run
  [model payable]
  (save payable))
