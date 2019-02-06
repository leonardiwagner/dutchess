(defproject dutchess "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"] [enlive "1.1.5"]]
  :main ^:skip-aot dutchess.main
  :jvm-opts ["-Xmx6g"]
  :target-path "target/%s")
