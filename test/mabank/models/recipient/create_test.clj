(ns mabank.models.recipient.create-test
  (:require [mabank.db :as db]
            [expectations :refer :all]
            [datomic.api :as d]
            [mabank.models.recipient.create :refer :all]
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

(def recipient-req { :json-params {:document_number "1234"
                                     :name "Cameron Howe"}})

(expect {:name "Cameron Howe" :document_number "1234"}
        (with-redefs [conn fake-conn]
          (do
            (parse-string (run recipient-req) true))))
