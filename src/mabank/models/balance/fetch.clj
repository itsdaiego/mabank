(ns mabank.models.balance.fetch
  (:require [mabank.db :as db]
            [datomic.api :as d]
            [cheshire.core :refer :all]))

(defn find-balance
  [req]
  (let [recipient-id (read-string(get-in req [:path-params :recipient-id]))]
    (d/q '[:find ?e ?amount ?status
           :in $ ?recipient-id
           :where [?e :balance/recipient ?recipient-id]
           [?e :balance/amount ?amount]
           [?e :balance/status ?status]] 
         (d/db db/conn) recipient-id)))

(defn sum-amount
  [contents]
  (reduce + (map second contents)))

(defn get-status
  [contents]
  (->>
    contents
    (map (fn [item] (nth item 2)))
    first))

(defn vector-to-hashmap
  ([] [])
  (contents (hash-map :amount (sum-amount contents)
                      :status (get-status contents))))

(defn run
  [req]
  (-> (find-balance req)
      (vector-to-hashmap)
      (generate-string)))
