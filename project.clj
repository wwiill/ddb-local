(defproject ddb-local "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1" :scope "provided"]
                 [com.cognitect.aws/api "0.8.630" :scope "provided"]
                 [com.amazonaws/DynamoDBLocal "1.20.0" :scope "provided"]
                 [kosmos/kosmos-dynamodb-local-native "1.0.0" :native-prefix "" :scope "provided"]]
  :repl-options {:init-ns ddb-local.core}
  :repositories {"amazon" {:url "https://s3-us-west-2.amazonaws.com/dynamodb-local/release"}})
