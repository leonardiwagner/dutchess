(ns dutchess.main
  (:gen-class)
  (:require [dutchess.subtitle-files-reader :refer [get-subtitles-lines]])
  (:require [dutchess.subtitle-line-parser :refer [parse]]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (get-subtitles-lines "./subtitles-example" (fn [line] (parse line))))
