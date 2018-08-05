(ns mabank.db
  (:require [datomic.api :as d]))

(def uri "datomic:free://localhost:4334/mabank-db")
(def conn (d/connect uri))

(defn transact-schema
  []
  (let [schema (load-file "resources/datomic/schema.edn")]
    (d/transact conn schema)))

(transact-schema)
