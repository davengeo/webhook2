(ns webhook2.handler
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :refer [resources not-found]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.logger.onelog :as logger]
            [onelog.core :as onelog]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
;[webhook2.eureka :as eureka]
            [webhook2.webhook :as webhook]
            [cheshire.core :refer :all]))

(defroutes app-routes
           (GET "/" [] (encode {:webhook "up" :baz 5} {:pretty true}))
           (POST "/webhook" request (webhook/webhook request))
           (resources "/public")
           (not-found "Not Found"))

(onelog/start!)
(onelog/set-debug!)
;(eureka/register "localhost" 8888 "webhook2" 3000)

(defn wrap-content-json [h]
  (fn [req] (assoc-in (h req) [:headers "Content-Type"] "application/json; charset=utf-8")))

(def app
  (-> app-routes
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))
      (logger/wrap-with-logger)
      (wrap-content-json)
      (wrap-json-body {:keywords? true :bigdecimals? true})
      (wrap-json-response)
      ))

