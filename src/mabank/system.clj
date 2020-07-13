(ns mabank.system
  (:require [com.stuartsierra.component :as component]
            [mabank.ports.http :as http]
            [mabank.ports.storage :as storage]
            [clojure.tools.logging :refer [error]]))

(def ^:redef system
  "Holds our system."
  nil)

(def system (atom nil))

(defn build-system
  "Defines our system map."
  []
  (try
    (-> (component/system-map
         :storage (storage/start-storage)
         :http (component/using (http/create-server) [:storage])))
    (catch Exception e
      (error "Error building system" e))))

(defn init-system
  []
  (let [sys (build-system)]
    (alter-var-root #'system (constantly sys))))

(defn stop!
  "Stop system"
  []
  (component/stop system)
  (alter-var-root #'system component/stop-system))

(defn start!
  "Start system"
  []
  (alter-var-root #'system component/start-system)
  (println "System started"))
