(ns webhook2.handler
  (:require [compojure.core :refer [defroutes GET POST]]
            [compojure.route :refer [resources not-found]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.logger.onelog :as logger]
            [cheshire.core :refer :all]
            [onelog.core :as onelog])
  (:gen-class))

(defroutes app-routes
           (GET "/" [] "hello world!")
           (POST "/webhook" request (str request))
           (resources "/public")
           (not-found "Not Found"))

(onelog/set-debug!)
(onelog/start!)

(def app
  (-> app-routes
      (wrap-defaults (assoc-in site-defaults [:security :anti-forgery] false))
      (logger/wrap-with-logger)))