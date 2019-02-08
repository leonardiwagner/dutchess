(ns dutchess.data-reader
  (:require [clojure.java.io :as io])
  (:require [dutchess.words-map-parser :as words-map-parser])
  (:require [dutchess.subtitle-line-parser :as subtitle-line-parser]))

(defn- get-directory-files [path]
  (-> path
    (clojure.java.io/file)
    (.listFiles)
    (->>
      (map #(.getAbsolutePath %)))))

(defn- lines->words-map [lines has-to-read-line-function]
  (reduce
    (fn [acc, line]
      (if (has-to-read-line-function line)
        (-> line
          (clojure.string/split #" ")
          (words-map-parser/words-coll->words-map)
          (->>
            (concat acc)))
        acc))
    []
    lines))

(defn- get-files-words-map [files has-to-read-line-function]
  (reduce
    (fn [acc, file]
      (printf "\rreading file [%s]..." file)
      (with-open [reader (clojure.java.io/reader file)]
        (-> reader
          (line-seq)
          (lines->words-map has-to-read-line-function)
          (->>
            (concat acc)))))
    []
    files))

(defn- read-words-from-path [path has-to-read-line-function]
  (-> path
    (get-directory-files)
    (get-files-words-map has-to-read-line-function)))

(defn read-books-words [path]
  (read-words-from-path path (fn [_] true)))

(defn read-subtitles-words [path]
  (read-words-from-path path subtitle-line-parser/subtitle-line?))
