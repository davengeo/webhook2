(defproject webhook2 "0.1.0-SNAPSHOT"
  :description "webhook for couchbase gw"
  :url "http://davengeo.com/"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.2.385"]
                 [compojure "1.5.1"]
                 [ring "1.3.2"]
                 [ring/ring-defaults "0.2.1"]
                 [ring-logger-onelog "0.7.6"]
                 [ring/ring-json "0.4.0"]
                 [eureka-client "0.2.0"]
                 [http-kit "2.1.18"]
                 [com.couchbase.client/java-client "2.3.1"]
                 [clj-http/clj-http "3.1.0"]]
  :plugins [[lein-ring "0.9.7"]
            [lein-ancient "0.5.5"]]
  :ring {:handler webhook2.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]
                        [reloaded.repl "0.2.2"]]
         }})
