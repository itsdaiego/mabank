(ns mabank.models.transaction.fetch
  (:require [mabank.db :as db]
            [datomic.api :as d]
            [cheshire.core :refer :all]))

(def conn db/conn)

(defn find-transactions
  []
  (d/q '[:find ?e ?amount ?installments ?recipient-id ?status
         :where [?e :transaction/amount _]
         [?e :transaction/amount ?amount]
         [?e :transaction/installments ?installments]
         [?e :transaction/recipient ?recipient-id]
         [?e :transaction/status ?status]]
       (d/db conn)))

(defn vector-to-hashmap
  [contents]
  (cond
    (empty? contents) []
    (not (empty? contents)) (mapv (fn [item] {:id (first item) 
                                              :amount (second item)
                                              :installments (nth item 2)
                                              :recipient-id (nth item 3)
                                              :status (nth item 4)
                                              })
                                  contents)))

(defn run
  []
  (-> (find-transactions)
      (vector-to-hashmap)
      (generate-string)))
