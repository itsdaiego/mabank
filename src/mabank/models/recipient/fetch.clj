(ns mabank.models.recipient.fetch
  (:require [mabank.db :as db]
            [datomic.api :as d]
            [cheshire.core :refer :all]))

(def first-recipient [
                      {:recipient/name "fulano1"
                       :recipient/document_number "01.266.392/0001-06"}
                      ])

(defn find-recipients
  []
  (d/q '[:find ?name ?document_number
         :where [_ :recipient/name ?name]
         [_ :recipient/document_number ?document_number]]
       (d/db db/conn)))

(defn vector-to-hashmap
  [contents]
  (cond
    (empty? contents) []
    (not (empty? contents)) (mapv (fn [item] {:name (first item)
                    :document_number (last item)})
        contents)))

(defn run
  []
  (-> (find-recipients)
      (vector-to-hashmap)
      (generate-string)))
