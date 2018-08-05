(ns mabank.server
  (:require [mabank.service :as service]
            [io.pedestal.http :as bootstrap]))

(def service-instance nil)

(defn create-server
  [& [opts]]
  (alter-var-root #'service-instance
                  (constantly (bootstrap/create-server (merge service/service opts)))))

(defn -main [& args]
  (println "Creating server...")
  (create-server)
  (println "Server created. Awaiting connections.")
  (bootstrap/start service-instance))
