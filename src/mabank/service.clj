(ns mabank.service
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.body-params :as body-params]
            [ring.util.response :as ring-resp]
            [io.pedestal.http.route.definition :refer [defroutes]]
            [mabank.models.recipient.fetch :as recipient-fetch]
            [mabank.models.recipient.create :as recipient-create]
            [mabank.models.transaction.create :as transaction-create]))


(defn get-recipients
  [req]
  (ring-resp/content-type (ring-resp/response @(future(recipient-fetch/run))) "application/json"))

(defn create-recipient
  [req]
  (ring-resp/content-type (ring-resp/response @(future(recipient-create/run req))) "application/json"))

(defn create-transaction
  [req]
  (ring-resp/content-type (ring-resp/response @(future(transaction-create/run req))) "application/json"))


(defn get-health-check
  [req]
  (ring-resp/response ""))

(defroutes routes
  [[["/_health-check" {:get get-health-check}]
    ["/recipients" {:get get-recipients}]
    ["/recipients" {:post create-recipient}
     ^:interceptors [(body-params/body-params)]]
    ["/transactions" {:post create-transaction}
     ^:interceptors [(body-params/body-params)]]]])

(def service {:env :prod
              ::http/routes routes
              ::http/resource-path "/public"
              ::http/type :jetty
              ::http/port 8080
              ::http/container-options {:h2c? true
                                        :h2? false
                                        :ssl? false}})

