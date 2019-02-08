(ns dutchess.main
  (:require [dutchess.words-frequency :as words-frequency]))


(defn -main
  [& args]
  (case (first args)
    "words-book" (println (words-frequency/get-frequency "./data-example/books"))

    ; "parse" (println (parse-words-file "words.txt"))
    ; "word" (println (read-word "zijn"))
    ; "definitions" (println (get-by-file "./result.txt"))
    ; "together" (get "./words.txt")
    (println "command not found")))
