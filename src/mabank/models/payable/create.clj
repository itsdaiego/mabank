(ns mabank.models.payable.create
  (:require [mabank.db :as db]
            [datomic.api :as d]
            [cheshire.core :refer :all]
            [clj-time.core :as time-core]
            [clj-time.coerce :as time-coerce]
            [clj-time.format :as time-format]))


(defn calculate-amount
  [transaction]
  (/ (get transaction :amount) (get transaction :installments)))

(def mdr-rate 0.05)

(defn calculate-fee
  [transaction]
  (long (* (get transaction :amount) mdr-rate)))

(defn calculate-payment-date
  [installment]
  (time-coerce/to-date (time-core/plus (time-format/parse 
                    (time-format/formatters :year-month-day) (str (time-core/today)))
                  (time-core/days (* 30 installment)))))

(def default-status "waiting-payemnt")

(defn save
  [transaction current-installment]
  (let [payable (hash-map :amount (calculate-amount transaction)
                          :fee (calculate-fee transaction)
                          :installment current-installment
                          :payment-date (calculate-payment-date current-installment)
                          :recipient-id (get transaction :recipient-id))]

    @(d/transact db/conn [{
                           :payable/amount (get payable :amount)
                           :payable/fee (get payable :fee)
                           :payable/installment (get payable :installment)
                           :payable/payment-date (get payable :payment-date)
                           :payable/recipient (get payable :recipient-id)
                           :payable/status default-status
                           }])))

(defn run
  [db-result transaction]
   (map (partial save transaction) 
       (range (get transaction :installments)))
   ;; TODO: find better way of dealing with add-hook function
   (generate-string transaction))
