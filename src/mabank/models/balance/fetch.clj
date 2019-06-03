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

(defn vector-to-hashmap
  [contents]
  (cond
    (empty? contents) []
    (not (empty? contents)) (hash-map :amount (reduce + (doall (map (fn [item] (second item)) contents)))
                                      :status (get-in contents [0 2])))) 

(defn run
  [req]
  (-> (find-balance req)
      (vector-to-hashmap)
      (generate-string)))
