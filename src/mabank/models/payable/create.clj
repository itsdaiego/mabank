(ns mabank.models.payable.create
  (:require [mabank.db :as db]
            [mabank.models.balance.create :as balance-create]
            [datomic.api :as d]
            [cheshire.core :refer :all]
            [clj-time.core :as time-core]
            [clj-time.coerce :as time-coerce]
            [clj-time.format :as time-format]
            [robert.hooke :as hooke]))


(def mdr-rate 0.05)

(def default-status "waiting-payemnt")

(def new-id (d/tempid :db.part/user))

(defn calculate-amount
  [transaction]
  (/ (:amount transaction) (:installments transaction)))

(defn calculate-fee
  [payable-amount]
  (long (* payable-amount mdr-rate)))

(defn calculate-payment-date
  [installment]
  (time-coerce/to-date (time-core/plus (time-format/parse 
                    (time-format/formatters :year-month-day) (str (time-core/today)))
                  (time-core/days (* 30 installment)))))



(defn save
  [transaction current-installment]
  (let [payable-amount (calculate-amount transaction)
        payable (hash-map :amount payable-amount
                          :fee (calculate-fee payable-amount)
                          :installment current-installment
                          :payment-date (calculate-payment-date current-installment)
                          :recipient-id (:recipient-id transaction)
                          :transaction-id (:transaction-id transaction))

        tx-result @(d/transact db/conn [{:db/id new-id
                                          :payable/amount (:amount payable)
                                          :payable/fee (:fee payable)
                                          :payable/installment (:installment payable)
                                          :payable/payment-date (:payment-date payable)
                                          :payable/recipient (:recipient-id payable)
                                          :payable/transaction (:transaction-id payable)
                                          :payable/status default-status
                                          }])]

    (assoc payable  :payable-id (d/resolve-tempid (:db-after tx-result) 
                                                  (:tempids tx-result) new-id))))

(defn build
  [payable]
  ())


(defn save-payables
  [transaction]
  (doall (map (partial save transaction)
              (drop 1 (range (inc (:installments transaction)))))))

(defn run
  [model transaction]
  (-> (save-payables transaction)
      (build))
  (generate-string transaction))

;; TODO fix hooke args
(hooke/add-hook #'build #'balance-create/run)
