(ns mabank.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [ring.util.response :as ring-resp]
            [mabank.models.recipient :as recipient]))


(def recipients-result (future(recipient/get-recipients)))

(defn get-recipients
  [request]
  (ring-resp/response (str @recipients-result)))

(defn health-check
  [request]
  (ring-resp/response ""))

(def common-interceptors [(body-params/body-params) http/html-body])
(def common-interceptors-json [(body-params/body-params) http/json-body])

(def routes #{["/_health-check" :get (conj common-interceptors `health-check)]
              ["/recipients" :get (conj common-interceptors `get-recipients)]})

(def service {:env :prod
              ::http/routes routes
              ::http/resource-path "/public"
              ::http/type :jetty
              ::http/port 8080
              ::http/container-options {:h2c? true
                                        :h2? false
                                        :ssl? false}})

