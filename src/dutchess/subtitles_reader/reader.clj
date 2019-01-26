(ns dutchess.subtitles-reader.reader
  (:require [dutchess.subtitles-reader.files-reader :refer [get-subtitles-lines]])
  (:require [dutchess.subtitles-reader.text-saver :refer [save]]))

(defn read-subtitles [path]
  (get-subtitles-lines path (fn [line] (save line))))
