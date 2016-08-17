;
; Copyright (c) 2016.
; me@davengeo.com
; MIT License https://opensource.org/licenses/MIT
;

(ns webhook2.config
  (:require [consul.core :as consul]
            [consul.component :as service-component]
            [onelog.core :as onelog]
            [clojure.core.async :as async]))

(onelog/info (consul/kv-get :local "tonto"))

(onelog/info (consul/agent-register-service :local {:id "webhook2-3000" :service-name "webhook2"}))

