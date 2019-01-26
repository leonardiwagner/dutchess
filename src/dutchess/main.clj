(ns dutchess.main
  (:gen-class)
  (:require [dutchess.subtitles-reader.reader :refer [read-subtitles]])
  (:require [dutchess.parser.text-parser :refer [parse-file]]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (case (first args)
    "read" (read-subtitles "/home/wagner/Documents/result")
    "parse" (println (parse-file "result.txt"))
    (println "command not found")))
