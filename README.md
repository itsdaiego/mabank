# mabank
That's  "ma" fictional bank system written in clojure!

### Installation ###

- Install [Leiningen](https://leiningen.org/)

- [Download](https://my.datomic.com/downloads/free) the datomic-free-0.9.5697.zip file and extract it.


### How to use ###

Inside the `project.clj` file, change <path_to_datomic-free-0.9.5697> to the datomic folder's path, example: `/home/myusername/.lein/datomic-free-0.9.5697`

Run the following commands:

- lein datomic start

- lein datomic initialize

- lein run

Note: It might throw an exception if you have jdk11


### Running tests ###

`lein autoexpect`
