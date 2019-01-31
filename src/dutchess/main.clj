(ns dutchess.main
  (:gen-class)
  (:require [dutchess.subtitles-words-reader.reader :refer [read-subtitles-words]])
  (:require [dutchess.parser.text-parser :refer [parse-words-file]]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (case (first args)
    ;"read" (read-subtitles "/home/wagner/Documents/result")
    "read" (read-subtitles-words "./subtitles-example")
    "parse" (println (parse-words-file "words.txt"))
    (println "command not found")))
