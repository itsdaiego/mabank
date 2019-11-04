(ns mabank.models.recipient.fetch-test
  (:require [mabank.db :as db]
            [expectations :refer :all]
            [datomic.api :as d]
            [mabank.models.helper.recipient-test :as mock-recipient]
            [mabank.models.recipient.fetch :refer :all]
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
    (hash-map :name (:name (first parsed-response))
              :document_number (:document_number (first parsed-response)))))

(expect {:name "Cameron Howe" :document_number "1234567890"}
		(with-redefs [conn fake-conn]
		  (do
            (mock-recipient/create fake-conn)
            (parse-response (run)))))
