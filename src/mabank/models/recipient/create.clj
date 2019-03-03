(ns mabank.models.recipient.create
  (:require [mabank.db :as db]
            [datomic.api :as d]
            [cheshire.core :refer :all]))


(defn parse-to-hashmap
  [req]
  (let [params (get req :json-params)]
    (hash-map :name (get params :name)
              :document_number (get params :document_number))))
  

(defn save
  [params]
  @(d/transact db/conn [{
                        :recipient/name (get params :name)
                        :recipient/document_number (get params :document_number)
                        }])
  params)

(defn build-response
  [recipient]
  (-> (hash-map :name (get recipient :name)
                :document_number (get recipient :document_number))
      (generate-string)))

(defn run
  [req]
  (-> (parse-to-hashmap req)
      (save)
      (build-response)))
