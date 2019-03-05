(ns mabank.models.payable.fetch
  (:require [mabank.db :as db]
            [datomic.api :as d]
            [cheshire.core :refer :all]))


(defn find-payables
  []
  (d/q '[:find ?e ?amount ?fee ?installment ?recipient-id ?status ?payment-date
         :where [?e :payable/amount _]
         [?e :payable/amount ?amount]
         [?e :payable/fee ?fee]
         [?e :payable/installment ?installment]
         [?e :payable/recipient ?recipient-id]
         [?e :payable/status ?status]
         [?e :payable/payment-date ?payment-date]]
       (d/db db/conn)))

(defn vector-to-hashmap
  [contents]
  (cond
    (empty? contents) []
    (not (empty? contents)) (mapv (fn [item] {:id (first item) 
                                              :amount (second item)
                                              :fee (nth item 2)
                                              :installment (nth item 3)
                                              :recipient-id (nth item 4)
                                              :status (nth item 5)
                                              :payment-date (nth item 6)
                                              })
                                  contents)))

(defn run
  []
  (-> (find-payables)
      (vector-to-hashmap)
      (generate-string)))
