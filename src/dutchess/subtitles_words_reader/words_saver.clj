(ns dutchess.subtitles-words-reader.words-saver
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

(defn cleanup-text [text]
  (let [textWithoutTags (clojure.string/replace text #"<[^>]*>" "")
        textWithoutPonctuation (clojure.string/replace textWithoutTags  #"[!|.|,|\"|?|:]" "")]
    textWithoutPonctuation))

(defn line-to-words [line]
  (let [lowerCaseLine (lower-case line)
        cleanLine (cleanup-text lowerCaseLine)
        words (clojure.string/split cleanLine #" ")]
       (reduce
         (fn [a,b] (str (clojure.string/trim a) "\n" (clojure.string/trim b) "\n"))
         words)))

(defn write-text-to-file [line]
  (spit "result.txt" (line-to-words line) :append true :encoding "UTF-8"))

(defn save-line-words [line]
  (if (is-text? line)
    (write-text-to-file line)))
