(ns webhook2.webhook
  (:require
    [clojure.core.async :refer :all]
    [onelog.core :as logger]
    [ring.util.response :refer [response]])
  (:import (com.couchbase.client.java CouchbaseCluster Bucket)))

(def cbinit (let [bucket-name "sync_gateway", cluster (CouchbaseCluster/create)]
                   (.openBucket cluster bucket-name)))

(def door (chan))

(defn- exists [id] (.exists cbinit id))

(defn- extract [keyz, request] (-> request
                                   (:body)
                                   (keyz)))

(defn process [request]  (let [_id (extract :_id request)]
                           ( do
                            (logger/info _id)
                            (logger/info (exists _id))
                            (>!! door (:body request))
                            (response  (:body request))
                            )
                           ))

(def event-loop (go
                  (loop []
                       (when-some [val (<! door)]
                         (logger/info (str "Received message:" val))
                         (recur)
                         )
                       )))