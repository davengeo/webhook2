(ns webhook2.handler
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :refer [resources not-found]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.logger.onelog :as logger]
            [cheshire.core :refer :all]
            [onelog.core :as onelog]))

(defroutes app-routes
           (GET "/" [] (encode {:webhook "up" :baz 5} {:pretty true}))
           (POST "/webhook" request (str request))
           (resources "/public")
           (not-found "Not Found"))

(onelog/start!)
(onelog/set-debug!)

(defn wrap-content-json [h]
  (fn [req] (assoc-in (h req) [:headers "Content-Type"] "application/json; charset=utf-8")))

(def app
  (-> app-routes
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))
      (logger/wrap-with-logger)
      (wrap-content-json)))