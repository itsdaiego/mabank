(ns mabank.models.helper.recipient-test
  (:require [datomic.api :as d]))


(def new-id (d/tempid :db.part/user))

(defn create
  [conn]
  (let [tx-result @(d/transact conn [{:db/id new-id
                                      :recipient/name "Cameron Howe"
                                      :recipient/document_number "1234567890"
                                      }])]
    (hash-map :recipient-id (d/resolve-tempid (:db-after tx-result) 
                                                         (:tempids tx-result) new-id))))
