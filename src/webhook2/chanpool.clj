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
  "create a pair {:n chan} where n is any number"
  (into {} {(keyword (str n)) (async/chan)}))

(defn create-pool [n]
  "create a hashmap composed of the merge of pairs from
  {:0 chan} to {:n-1 chan} like {:0 chan, :1 chan, ...,:n-1 chan}
  where n is the number of pairs"
  (reduce merge {}
          (for [n (take n (range))]
            (create-pair n))))

(defn random-channel [pool]
  "result a channel from a pool corresponding to a random key "
  ((keyword (str (rand-int (- (count pool) 1)))) pool))

(defn get-chan [n pool]
  "result the nth channel from a pool"
  ((keyword (str n)) pool))

(defn pool-put! [value pool]
  "put some value into the pool, if the selected channel is parked
  it retries until succesful"
  (async/go
    (while
      (not (async/offer! (random-channel pool) value)))))

(defn create-listener [nmax pool fn]
  "create nmax listener to the pool when any value arrives to any channel
  then the functionfn is applied with the value as argument"
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