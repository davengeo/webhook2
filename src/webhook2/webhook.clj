;
; Copyright (c) 2016.
; me@davengeo.com
; MIT License https://opensource.org/licenses/MIT
;

(ns webhook2.webhook
  (:require
    [clojure.core.async :refer :all]
    [cheshire.core :refer :all]
    [onelog.core :as logger]
    [webhook2.chanpool :refer :all]
    [webhook2.couchbase :refer :all]
    [ring.util.response :refer [response status]]))

(def pool (create-pool 20))

(defn just-log [value n] (logger/info (str "Received message:" value " in listener:" n)))

(def listeners (create-listener 100 pool just-log))

(defn- extract [keyz, request] (-> request
                                   (:body)
                                   (keyz)))

(defn- bad-response [msg] (-> (response (encode {:message msg}))
                              (status 400)))

(defn- accepted [id] (-> (response (encode {:_id id :status "accepted"}))
                         (status 201)))

(defn process [request]
  (let [_id (extract :_id request)]
                          (cond
                            (nil? _id) (bad-response "not valid id")
                            (not (exists _id)) (bad-response "not existing id")
                            ;open the door
                            :else
                              (do (pool-put! (:body request) pool)
                                      (accepted _id))
                          )))