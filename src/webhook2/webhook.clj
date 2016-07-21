(ns webhook2.webhook
  (:require
    [onelog.core :as logger]
    [ring.util.response :refer [response]])
  (:import (com.couchbase.client.java CouchbaseCluster Bucket)))

(defn- cbinit [bucket-name] (let [cluster (CouchbaseCluster/create)]
                   (.openBucket cluster bucket-name)))

(defn- exists [id, bucket-name] (.exists (cbinit bucket-name) id))

(defn- extract [keyz, request] (-> request
                                   (:body)
                                   (keyz)))

(defn webhook [request] (do (logger/info (extract :_id request))
                            (logger/info (exists (extract :_id request) "sync_gateway"))
                            (response  (:body request))
                            ))


