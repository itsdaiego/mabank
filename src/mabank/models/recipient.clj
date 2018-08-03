(ns mabank.models.recipient
  (:require [mabank.db.connector :as db]
            [datomic.api :as d]))

(def first-recipient [
                      {:recipient/name "fulano1"
                       :recipient/cnpj "01.266.392/0001-06"}
                      ])

(defn create-recipient
  []
  (d/transact db/conn first-recipient))

(defn find-recipients
  [param]
  (d/q '[:find ?name ?cnpj
         :where [_ :recipient/name ?name]
         [_ :recipient/cnpj ?cnpj]]
       (d/db db/conn)))

(defn parse-to-json
  [contents]
  (mapv (fn [item] {:name (first item)
                   :document_number (last item)})
       contents))

(defn get-recipients
  []
  (-> (create-recipient)
      (find-recipients)
      (parse-to-json)))
