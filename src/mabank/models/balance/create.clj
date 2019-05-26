(ns mabank.models.balance.create
  (:require [mabank.db :as db]
            [datomic.api :as d]
            [cheshire.core :refer :all]))

(defn save
  [payables]
  (map #(@(d/transact db/conn [{:db/id (d/tempid :db.part/user)
                                         :balance/status "future-payment"
                                         :balance/amount (get % :amount)
                                         :balance/fee (get % :fee)
                                         :balance/recipient (get % :recipient-id)
                                         :balance/payable (get % :payable-id)
                                         }])) payables))

(defn run
  [model payable]
  (save payable))
