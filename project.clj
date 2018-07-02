(defproject mabank "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.datomic/datomic-free "0.9.5697"]]

  :main ^:skip-aot mabank.core
  :target-path "target/%s"
  :datomic {:schemas ["resources/datomic" ["schema.edn"]]}

  :profiles {:uberjar {:aot :all}
             :dev
             {:datomic {:config "resources/datomic/free-transactor-template.properties"
                        :db-uri "datomic:free://localhost:4334/mabank-db"}}})
; :profiles {}

