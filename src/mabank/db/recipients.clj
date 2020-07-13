(ns mabank.db.recipients
  (:require [mabank.ports.protocols.storage-client :as storage-client]))

(defn create-recipient!
  [storage payload]
  (let [recipient-id (storage-client/create-recipient! storage payload)]
    (assoc payload :id recipient-id)))

(defn fetch-recipients [storage]
  (storage-client/fetch-recipients storage))
