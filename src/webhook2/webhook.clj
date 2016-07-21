(ns webhook2.webhook
  (:require
      [onelog.core :as logger]
      [ring.util.response :refer [response]]
            ))

(defn- extract [keyz, request] (-> request
                                   (:body)
                                   (keyz)))

(defn webhook [request] (do (logger/info (:body request))
                            (response  (:body request))
                            ))

