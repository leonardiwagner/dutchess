(ns dutchess.subtitles-reader.text-saver
  (:require [clojure.string :refer [lower-case]]))

(def subtitle-index-match #"^[0-9]+$")
(def subtitle-timestamp-match #"^[0-9]+.+\s-->\s+[0-9]+.*")
(def subtitle-text #".*[a-z|A-Z].*")

(defn is-text? [line]
  (if
    (and
      (not (re-matches subtitle-index-match line))
      (not (re-matches subtitle-timestamp-match line))
      (re-matches subtitle-text line))
    true
    false))

(defn write-text-to-file [line]
  (let [lowerCaseText (lower-case line)]
    (spit "result.txt" (str lowerCaseText "\n") :append true :encoding "ISO-8859-1")))

(defn save [line]
  (if (is-text? line)
    (write-text-to-file line)))
