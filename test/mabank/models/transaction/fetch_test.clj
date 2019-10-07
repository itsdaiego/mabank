(ns mabank.models.transaction.fetch-test
  (:require [mabank.db :as db]
            [expectations :refer :all]
            [datomic.api :as d]
            [mabank.models.helper.recipient-test :as mock-recipient]
            [mabank.models.helper.transaction-test :as mock-transaction]
            [mabank.models.transaction.fetch :refer :all]))

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

(expect "[{\"id\":17592186045420,\"amount\":100,\"installments\":1,\"recipient-id\":17592186045418,\"status\":\"waiting-payemnt\"}]"
		(with-redefs [conn fake-conn]
		  (do
            (let [recipient-id (get (mock-recipient/create fake-conn) :recipient-id)]
              (mock-transaction/create recipient-id fake-conn))
            (run))))
