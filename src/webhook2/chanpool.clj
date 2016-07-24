;
; Copyright (c) 2016.
; me@davengeo.com
; MIT License https://opensource.org/licenses/MIT
;

(ns webhook2.chanpool
  (:require
    [clojure.core.async :as async]
    [onelog.core :as logger]))

(defn- create-pair [n]
  (into {} {(keyword (str n)) (async/chan)}))

(defn- create-pool [n]
  (reduce merge {}
        (for [n (take n (range))]
          (create-pair n))))

(defn- random-channel [coll]
  ((keyword (str (rand-int (- (count coll) 1)))) coll))

(def pool (create-pool 10))

(defn get-random-chan [] (random-channel pool))

(defn get-chan [n] ((keyword (str n)) pool))

(defn listener [n nmax]
  (if (< n nmax)
    (do
      (async/go-loop []
               (when-some [val (async/<! (get-chan n))]
                 (logger/info (str "Received message:" val " in listener:" n))
                 (recur)))
      (logger/info (str "listener " n " created"))
      (recur (+ 1 n) nmax))))

(async/thread (listener 0, 10))


