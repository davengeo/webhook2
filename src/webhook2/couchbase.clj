;
; Copyright (c) 2016.
; me@davengeo.com
; MIT License https://opensource.org/licenses/MIT
;

(ns webhook2.couchbase
  (:import (com.couchbase.client.java CouchbaseCluster Bucket)))


(def cbinit (let [bucket-name "sync_gateway", cluster (CouchbaseCluster/create)]
              (.openBucket cluster bucket-name)))

(defn exists [id] (.exists cbinit id))