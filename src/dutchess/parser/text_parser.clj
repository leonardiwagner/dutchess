(ns dutchess.parser.text-parser
  (:require [clojure.java.io :as io]))

(defn line-to-word-maps [line]
  (let [words (clojure.string/split line #" ")
        wordMaps (map (fn [word] {(keyword word) 1}) words)]
    (reduce
      (fn [a, b] (merge-with + a b))
      wordMaps)))

(defn lines-to-word-maps [lines]
  (reduce
    (fn [acc line]
      (let [wordMaps (line-to-word-maps line)]
        (merge-with + acc wordMaps)))
    {}
    lines))

(defn sort-map-by-value [super-map]
  (into (sorted-map-by (fn [key1 key2] (let [val1 (super-map key1) val2 (super-map key2)] (cond (= val1 val2) (.compareTo key2 key1) (< val1 val2) 1 :else -1)))) super-map))

(defn save-word [text]
  (spit "words.txt" (str text "\n") :append true :encoding "ISO-8859-1"))

(defn parse-file [file]
    (with-open [rdr (clojure.java.io/reader file)]
      (let [lines (line-seq rdr)
            wordMaps (lines-to-word-maps lines)
            sortedWordMaps (sort-map-by-value wordMaps)
            words (seq sortedWordMaps)]
        (doseq [word words]
          (save-word (str (val word) " " (key word)))))))
