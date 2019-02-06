(ns dutchess.main
  (:gen-class)
  (:require [dutchess.subtitles-words-reader.reader :refer [read-subtitles-words]])
  (:require [dutchess.parser.text-parser :refer [parse-words-file]])
  (:require [dutchess.word-definition.dictionary :refer [read-word]])
  (:require [dutchess.word-definition.word-definition-finder :refer [get-by-file]])
  (:require [dutchess.words-together :refer [get]]))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (case (first args)
    ;"read" (read-subtitles "/home/wagner/Documents/result")
    "read" (read-subtitles-words "./subtitles-example")
    "parse" (println (parse-words-file "words.txt"))
    "word" (println (read-word "zijn"))
    "definitions" (println (get-by-file "./result.txt"))
    "together" (get "./words.txt")
    (println "command not found")))
