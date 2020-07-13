(ns mabank.main
  (:require [mabank.system :refer [init-system start!]])
  (:gen-class))

(defn -main [& args]
  (init-system)
  (start!))
