(ns mabank.ports.protocols.storage-client
  (:require [schema.core :as s]))

(defprotocol StorageClient
  (create-recipient! [storage payload])
  (fetch-recipients [storage]))

(def IStorageClient (s/protocol StorageClient))

