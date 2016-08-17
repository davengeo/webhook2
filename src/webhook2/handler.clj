(ns webhook2.handler
  (:use org.httpkit.server)
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :refer [not-found]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.logger.onelog :refer [wrap-with-logger]]
            [onelog.core :as onelog]
            [consul.core :as consul]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [webhook2.webhook :as webhook]
            [cheshire.core :refer :all]))

(onelog/start!)
(onelog/set-info!)

(defroutes app-routes
           (GET "/" [] (encode {:webhook "up" :bar 5} {:pretty true}))
           (POST "/webhook" request (webhook/process request))
           (not-found "Not Found"))

(defn wrap-content-json [h]
  (fn [req] (assoc-in (h req) [:headers "Content-Type"] "application/json; charset=utf-8")))

(def app
  (-> app-routes
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))
      (wrap-with-logger)
      (wrap-content-json)
      (wrap-json-body {:keywords? true :bigdecimals? true})
      (wrap-json-response)))

(run-server #'app {:port 3000})