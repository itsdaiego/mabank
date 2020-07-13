(ns mabank.adapter
  (:require [cheshire.core :refer :all]))

;; TODO should create based on dynamic fields 
;; [[17592186045550 nani 12345], [17592186045548 nani 12345]]
(defn vector->hashmap
  [items]
  (if (empty? items) 
    []
    (mapv (fn [item] {:id (first item) 
                      :name (second item)
                      :document_number (last item)})
          items)))

(defn vector->json
  [payload]
  (-> (vector->hashmap payload)
      (generate-string)))

(defn vector-errors->json-errors
  [payload]
  (generate-string {:errors payload}))

(defn hashmap->json
  [payload]
  (generate-string payload))
