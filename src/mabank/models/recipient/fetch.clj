(ns mabank.models.recipient.fetch
  (:require [mabank.db :as db]
            [datomic.api :as d]
            [cheshire.core :refer :all]))

(def conn db/conn)

; TODO https://stackoverflow.com/questions/14189647/get-all-fields-from-a-datomic-entity
(defn find-recipients
  []
  (d/q '[:find ?e ?name ?document_number
         :where [?e :recipient/name _]
         [?e :recipient/name ?name]
         [?e :recipient/document_number ?document_number]]
       (d/db conn)))

(defn vector-to-hashmap
  [contents]
  ([] [])
  (contents (mapv (fn [item] {:id (first item)
                              :name (second item)
                              :document_number (last item)})
                  contents)))

; (defn vector-to-hashmap
;   [contents]
;   (cond
;     (empty? contents) []
;     (not (empty? contents)) (mapv (fn [item] {:id (first item) 
;                                               :name (second item)
;                                               :document_number (last item)})
;                                   contents)))


(defn run
  []
  (-> (find-recipients)
      (vector-to-hashmap)
      (generate-string)))
