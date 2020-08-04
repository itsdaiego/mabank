# mabank
That's  "ma" fictional bank system written in clojure!

### Installation ###

- Install [Leiningen](https://leiningen.org/)

- [Download](https://my.datomic.com/downloads/free) the datomic-free-0.9.5697.zip file and extract it.


### How to use ###

1 - Start the transactor

```
~/.lein/datomic-free-0.9.5697.21/bin/transactor <path_to_project>/mabank/resources/datomic/free-transactor-template.properties

```
2 - Start the application

- lein run

### Running tests ###

`lein test`

### TODO 

- [] create integration and e2e tests
- [] create transfer model
- [] create balance model
