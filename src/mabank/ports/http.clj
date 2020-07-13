(ns mabank.ports.http
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [io.pedestal.http :as bootstrap]
            [ring.util.response :as ring-resp]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [com.stuartsierra.component :as component]
            [mabank.controllers.recipient :as recipient-controller]))

(def _storage (atom nil))

(defn get-health-check
  [req]
  (ring-resp/response ""))

(defn create-recipient!
  [req]
  (try
    (ring-resp/content-type 
      (ring-resp/status
        (ring-resp/response @(future(recipient-controller/create-recipient! @_storage (:json-params req))))
        201)
      "application/json")

    (catch Exception e
      (ring-resp/content-type 
        (ring-resp/status
          (ring-resp/response (-> (Throwable->map e) :data :cause))
          400)
        "application/json"))))

(defn fetch-recipients
  [req]
  (try
    (ring-resp/content-type 
      (ring-resp/status
        (ring-resp/response @(future(recipient-controller/fetch-recipients @_storage)))
        200)
      "application/json")
    (catch Exception e
      ;; TODO: should be a interceptor
      (println e)
      (throw e))))


(defroutes routes
  [[["/_health-check" {:get get-health-check}]
    ["/recipients" {:post create-recipient!}
     ^:interceptors [(body-params/body-params)]]
    ["/recipients" {:get fetch-recipients}]]])

(def service {:env :prod
              ::http/routes routes
              ::http/resource-path "/public"
              ::http/type :jetty
              ::http/port 8080
              ::http/container-options {:h2c? true
                                        :h2? false
                                        :ssl? false}})


(def service-instance nil)

(defn start-server []
  (println "Creating server...")
  (alter-var-root #'service-instance
                  (constantly (bootstrap/create-server (merge service))))
  (println "Server created. Awaiting connections.")
  (bootstrap/start service-instance))

(defrecord HttpServer [server storage]

  component/Lifecycle

  (start [this]
    (swap! _storage assoc :db storage)
    (assoc this :server (start-server)))

  ;; TODO: set service-instance to nil
  (stop [this]
    (reset! _storage)
    (assoc this :server nil)))

(defn create-server
  []
  (map->HttpServer {}))
