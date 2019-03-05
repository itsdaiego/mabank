(defproject mabank "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.datomic/datomic-free "0.9.5697"]
                 [lein-datomic "0.2.0"]
                 [io.pedestal/pedestal.service "0.5.2"]
                 [io.pedestal/pedestal.jetty "0.5.2"]
                 [ch.qos.logback/logback-classic "1.1.8"]
                 [org.slf4j/jul-to-slf4j "1.7.22"]
                 [org.slf4j/jcl-over-slf4j "1.7.22"]
                 [org.slf4j/log4j-over-slf4j "1.7.22"]
                 [robert/hooke "1.3.0"]
                 [clj-time "0.15.0"]
                 [cheshire "5.8.0"]] 

  :resource-paths ["config", "resources"]

  :main ^{:skip-aot true} mabank.server

  :target-path "target/%s"

  :plugins [[lein-datomic "0.2.0"]] 

  :datomic {:schemas ["resources/datomic" ["schema.edn"]]
            :install-location "/home/daiego/.lein/datomic-free-0.9.5697"}

  :profiles {:uberjar {:aot [mabank.server]}
             :dev
             {:datomic {:config "resources/datomic/free-transactor-template.properties"
                        :db-uri "datomic:free://localhost:4334/mabank-db"}}})
