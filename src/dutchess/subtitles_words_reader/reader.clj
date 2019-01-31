(ns dutchess.subtitles-words-reader.reader
  (:require [dutchess.subtitles-words-reader.files-reader :refer [get-subtitles-lines]])
  (:require [dutchess.subtitles-words-reader.words-saver :refer [save-line-words]]))

(defn read-subtitles-words [path]
  (get-subtitles-lines path (fn [line] (save-line-words line))))
