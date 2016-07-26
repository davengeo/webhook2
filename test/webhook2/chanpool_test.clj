;
; Copyright (c) 2016.
; me@davengeo.com
; MIT License https://opensource.org/licenses/MIT
;

(ns webhook2.chanpool-test
  (:use webhook2.chanpool)
  (:require
    [clojure.core.async :as async]
    [clojure.string :as str]
    [clojure.test :refer :all])
  (:import (clojure.core.async.impl.channels ManyToManyChannel)))

(def pool (create-pool 10))

(deftest pool_is_not_nil
  (is (not (nil? pool))))

(deftest pool_has_n_members
  (is (= (count pool) 10)))

(deftest pool_contains_chans
  (is (instance? ManyToManyChannel (:0 pool)))
  (is (instance? ManyToManyChannel (:1 pool)))
  (is (instance? ManyToManyChannel (:2 pool)))
  (is (instance? ManyToManyChannel (:3 pool)))
  (is (instance? ManyToManyChannel (:4 pool)))
  (is (instance? ManyToManyChannel (:5 pool)))
  (is (instance? ManyToManyChannel (:6 pool)))
  (is (instance? ManyToManyChannel (:7 pool)))
  (is (instance? ManyToManyChannel (get-chan 8 pool)))
  (is (instance? ManyToManyChannel (get-chan 9 pool)))
  (is (instance? ManyToManyChannel (get-random-chan pool))))

