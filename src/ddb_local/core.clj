(ns ddb-local.core
  (:require [cognitect.aws.credentials :as creds])
  (:import (com.amazonaws.services.dynamodbv2.local.main ServerRunner)
           (com.amazonaws.services.dynamodbv2.local.server DynamoDBProxyServer)
           (java.lang AutoCloseable)
           (java.net ServerSocket)))

(defn- random-port []
  (with-open [s (ServerSocket. 0)]
    (.getLocalPort s)))

(defn start-ddb
  ([port]
   (let [opts   (into-array String ["--inMemory" "-port" (str port)])
         server (ServerRunner/createServerFromCommandLineArgs opts)]
     (println "Starting ddb server on port" port)
     (.start server)
     {:port   port
      :server server}))
  ([] (start-ddb (random-port))))

(defn stop-ddb [this]
  (when-let [^DynamoDBProxyServer server (:server this)]
    (println "Stopping ddb server on port" (:port this))
    (.stop server)))

(defrecord DdbServer [port server]
  AutoCloseable
  (close
    [this]
    (stop-ddb this)))

(defn create-ddb
  ([port]
   (map->DdbServer (start-ddb port)))
  ([]
   (map->DdbServer (start-ddb))))

(defn client-opts [sys]
  {:api                  :dynamodb
   :region               "us-east-1" ;; must be valid region
   :credentials-provider (creds/basic-credentials-provider {:access-key-id     "dummy"
                                                            :secret-access-key "dummy"})
   :endpoint-override    {:protocol :http
                          :hostname "localhost"
                          :port     (:port sys)}})

(comment
  (def sys (start-ddb))

  (stop-ddb sys)

  (require '[cognitect.aws.client.api :as aws])

  (def client (aws/client (client-opts sys)))

  (aws/invoke client {:op :ListTables}))