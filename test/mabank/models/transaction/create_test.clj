(ns mabank.models.transaction.create-test
  (:require [mabank.db :as db]
            [expectations :refer :all]
            [datomic.api :as d]
            [mabank.models.helper.recipient-test :as mock-recipient]
            [mabank.models.transaction.create :refer :all]
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

(def transaction-req { :json-params {:amount 100
                                     :installments 1
                                     :recipient-id 123
                                     :transaction-id 321}})

(defn parse-response
  [response]
  (let [parsed-response (parse-string response true)]
    (hash-map :amount (:amount parsed-response)
              :installments (:installments parsed-response)
              :recipient-id (:recipient-id parsed-response))))

(expect {:amount 100 :installments 1 :recipient-id 123}
        (with-redefs [conn fake-conn]
          (do
            (parse-response (run transaction-req)))))
