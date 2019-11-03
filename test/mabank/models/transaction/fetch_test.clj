(ns mabank.models.transaction.fetch-test
  (:require [mabank.db :as db]
            [expectations :refer :all]
            [datomic.api :as d]
            [mabank.models.helper.recipient-test :as mock-recipient]
            [mabank.models.helper.transaction-test :as mock-transaction]
            [mabank.models.transaction.fetch :refer :all]
            [cheshire.core :refer :all]))

(defn create-empty-db
  []
  (let [uri "datomic:mem://mabank-db"]
    (d/delete-database uri)
    (d/create-database uri)
    (let [conn (d/connect uri)
          schema (load-file "resources/datomic/schema.edn")]
      (d/transact conn schema)
      conn)))

(def fake-conn (create-empty-db))

(defn parse-response
  [response]
  (let [parsed-response (parse-string response true)]
    (hash-map :amount (:amount (first parsed-response))
              :installments (:installments (first parsed-response))
              :status (:status (first parsed-response))
              :recipient-id (:recipient-id (first parsed-response)))))

(expect {:amount 100 :installments 1 :recipient-id 123 :status "waiting-payemnt"}
		(with-redefs [conn fake-conn]
		  (do
            (mock-recipient/create fake-conn)
            (mock-transaction/create fake-conn)
            (parse-response (run)))))
