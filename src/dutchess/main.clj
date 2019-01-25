(ns dutchess.main
  (:gen-class)
  (:require [dutchess.subtitle-files-reader :refer [get-subtitles-lines]]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (get-subtitles-lines "/home/wagner/Documents/result" (fn [line] (println line))))
