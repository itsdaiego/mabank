(ns mabank.models.transaction.create-test
  (:require [mabank.db :as db]
            [expectations :refer :all]
            [datomic.api :as d]
            [mabank.models.helper.recipient-test :as mock-recipient]
            [mabank.models.transaction.create :refer :all]))

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
                                     :installments 1}})

(expect "{\"amount\":100,\"installments\":1,\"recipient-id\":17592186045418,\"transaction-id\":17592186045420}"
        (with-redefs [conn fake-conn]
          (do
            (let [recipient-id (get (mock-recipient/create fake-conn) :recipient-id)]
              (run (assoc-in transaction-req [:json-params :recipient-id] recipient-id))))))
