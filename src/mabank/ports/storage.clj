(ns mabank.ports.storage
  (:require [com.stuartsierra.component :as component]
            [datomic.api :as d]
            [mabank.ports.protocols.storage-client :as storage-client]))

;; TODO: should be passed using component's constructor
(def uri "datomic:free://localhost:4334/mabankdb")
(def conn (d/connect uri))

(defrecord Datomic [storage]

  component/Lifecycle

  (start [this]
    (d/create-database uri)
    (let [schema (load-file "resources/datomic/schema.edn")]
      (d/transact conn schema))
    (assoc this :conn (d/connect uri) :db #(d/db conn)))

  (stop [this]
    (assoc this :conn nil :db nil))

  storage-client/StorageClient


  ;; TODO Find a solution that does not require a function per entity
  (create-recipient! [storage payload] 
    (let [tempid (d/tempid :db.part/user) 
          tx-result @(d/transact (:conn storage) [{:db/id tempid
                                                   :recipient/name (:name payload)
                                                   :recipient/document_number (:document-number payload)}])]
      (d/resolve-tempid (:db-after tx-result) 
                        (:tempids tx-result) tempid)))

  (fetch-recipients [storage]
    (d/q '[:find ?e ?name ?document_number
           :where [?e :recipient/name _]
           [?e :recipient/name ?name]
           [?e :recipient/document_number ?document_number]]
         (d/db (:conn storage)))))


(defn start-storage
  []
  (map->Datomic {}))

