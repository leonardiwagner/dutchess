(defproject dutchess "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [wiktionary "0.0.3-SNAPSHOT"]
                 [srt-to-text "0.0.1-SNAPSHOT"]
                 [text-frequency "0.0.3-SNAPSHOT"]]
  :main dutchess.core
  :repl-options {:init-ns dutchess.core}
  :jvm-opts ["-Xmx6g"]
  :target-path "target/%s")
