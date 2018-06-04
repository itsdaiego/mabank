(ns mabank.core
  (:gen-class)
  (:require [datomic.api :as d]))


(def conn nil)

(defn create-recipient
  [name]
  @(d/transact conn [{:db/id (d/tempid :db.part/recipient)
                      :recipients/name name}]))

(defn find-recipients
  []
  (d/q '[:find ?recipient-name
         :where [_ :recipients/name ?recipients/name]]))

(defn -main
  [& args]
  ((create-recipient "fulano")
  (find-recipients)))
