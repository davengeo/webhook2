(ns webhook2.webhook
  (:require
    [clojure.core.async :refer :all]
    [onelog.core :as logger]
    [cheshire.core :refer :all]
    [webhook2.chanpool :refer :all]
    [ring.util.response :refer [response status]])
  (:import (com.couchbase.client.java CouchbaseCluster Bucket)))

(def cbinit (let [bucket-name "sync_gateway", cluster (CouchbaseCluster/create)]
              (.openBucket cluster bucket-name)))

(defn- exists [id] (.exists cbinit id))

(defn- extract [keyz, request] (-> request
                                   (:body)
                                   (keyz)))

(defn- bad-response [msg] (-> (response (encode {:message msg}))
                              (status 400)))

(defn- accepted [id] (-> (response (encode {:_id id :status "accepted"}))
                         (status 201)))

(defn process [request] (let [_id (extract :_id request)]
                          (cond
                            (nil? _id) (bad-response "not valid id")
                            (not (exists _id)) (bad-response "not existing id")
                            ;open the door
                            :else (do (go (>! (get-random-chan) (:body request)))
                                      (accepted _id)))
                          ))