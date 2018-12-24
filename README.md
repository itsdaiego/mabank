# mabank
That's  "ma" fictional bank system written in clojure!

### Installation ###

- Install [Leiningen](https://leiningen.org/)

- [Download](https://my.datomic.com/downloads/free) the datomic-free-0.9.5697.zip file.

- Create a `.lein` folder at your home directory


Finally your should have a `profile.clj` file within the the `.lein` dir, it should looke like this:

```
{:user
 {:plugins [[lein-datomic "0.2.0"]]
  :datomic {:install-location "<your-path-to-this-file>datomic-free-0.9.5697"}}}

```

Note: It might throw an exception if you have jdk11


### How to use ###

Run the following commands:

- lein datomic start

- lein datomic initialize

- lein run
