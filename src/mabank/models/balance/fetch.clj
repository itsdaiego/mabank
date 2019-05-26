(ns mabank.models.balance.fetch
  (:require [mabank.db :as db]
            [datomic.api :as d]
            [cheshire.core :refer :all]))

(defn find-balance
  [req]
  (let [recipient-id (read-string(get-in req [:path-params :recipient-id]))]

  (d/q '[:find ?e (sum ?amount)
         :in $ ?recipient-id
         :where [?e :balance/recipient ?recipient-id]
         [?e :balance/amount ?amount]] 
       (d/db db/conn) recipient-id)))

(defn vector-to-hashmap
  [contents]
  (cond
    (empty? contents) []
    (not (empty? contents)) (mapv (fn [item] {
                                              :amount (first item)
                                              })
                                  contents)))

(defn run
  [req]
  (-> (find-balance req)
      (vector-to-hashmap)
      (generate-string)))
