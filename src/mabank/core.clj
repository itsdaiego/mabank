(ns mabank.core
  (:require [datomic.api :as d]))

(def uri "datomic:free://localhost:4334/mabank-db")
(def conn (d/connect uri))

(defn transact-schema
  []
  (let [schema (load-file "resources/datomic/schema.edn")]
    (d/transact conn schema)))

(def first-recipient [
                      {:recipient/name "fulano1"
                       :recipient/cnpj "01.266.392/0001-06"}
                      ])

(defn create-recipient
  [param]
  (d/transact conn first-recipient))

(defn find-recipients
  [param]
  (d/q '[:find ?name ?cnpj
         :where [_ :recipient/name ?name]
         [_ :recipient/cnpj ?cnpj]]
       (d/db conn)))

(defn print-content
  [content]
  (print content))

(defn -main
  [& args]
  (-> (transact-schema)
      (create-recipient)
      (find-recipients)
      (print-content)))
