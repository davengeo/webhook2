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

(defn create-pool [n]
  (reduce merge {}
          (for [n (take n (range))]
            (create-pair n))))

(defn random-channel [coll]
  ((keyword (str (rand-int (- (count coll) 1)))) coll))

(defn get-random-chan [pool] (random-channel pool))

(defn get-chan [n pool] ((keyword (str n)) pool))

(defn pool-put! [value pool]
  (async/go
    (while
      (not (async/offer! (get-random-chan pool) value)))))

(defn create-listener [nmax pool fn]
  (loop [n 0]
  (let [seq-ch (into [] (vals pool))]
    (if (< n nmax)
      (do
        (async/go-loop []
          (when-some [[val p] (async/alts! seq-ch)]
            (async/go (apply fn [val p])))
            (recur))
        (logger/info (str "listener " n " created"))
        (recur (+ 1 n))
        )
      ))))




