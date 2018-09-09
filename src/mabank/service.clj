(ns mabank.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [ring.util.response :as ring-resp]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [mabank.models.recipient :as recipient]))


(defn get-recipients
  [request]
  (ring-resp/content-type (ring-resp/response @(future(recipient/get-recipients))) "application/json"))

(defn get-health-check
  [request]
  (ring-resp/response ""))

(defroutes routes
  [[["/_health-check" {:get get-health-check}]
    ["/recipients" {:get get-recipients}]]])

(def service {:env :prod
              ::http/routes routes
              ::http/resource-path "/public"
              ::http/type :jetty
              ::http/port 8080
              ::http/container-options {:h2c? true
                                        :h2? false
                                        :ssl? false}})

