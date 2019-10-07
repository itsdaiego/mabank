(ns mabank.models.recipient.create
  (:require [mabank.db :as db]
            [datomic.api :as d]
            [cheshire.core :refer :all]))

(def conn db/conn)

(defn parse-to-hashmap
  [req]
  (let [params (:json-params req)]
    (hash-map :name (:name params)
              :document_number (:document_number params))))

(defn save
  [params]
  @(d/transact db/conn [{
                        :recipient/name (:name params)
                        :recipient/document_number (:document_number params)
                        }])
  params)

(defn build-response
  [recipient]
  (-> (hash-map :name (:name recipient)
                :document_number (:document_number recipient))
      (generate-string)))

(defn run
  [req]
  (-> (parse-to-hashmap req)
      (save)
      (build-response)))
