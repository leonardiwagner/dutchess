(ns dutchess.core
  (:require [dutchess.file-system :as fs]
            [srt-to-text.core :as srt-to-text]
            [text-frequency.core :as text-frequency]
            [wiktionary.core :as wiki]))

(defn cleanup-text [text]
  (-> text
      (clojure.string/lower-case)
      (clojure.string/replace #"' | '" "") ;remove unwanted chars around the word
      (clojure.string/replace #"‘|’" "'"))) ;padronize

(defn get-file-words [file]
  (let [subtitle (srt-to-text/parse-file file {:has-to-cleanup-text true})
        subtitle-text (srt-to-text/get-parsed-text subtitle)
        text (cleanup-text subtitle-text)
        words (text-frequency/count-words text)
        relevant-words (into {} (filter #(> (val %) 1) words))]
    relevant-words))

(defn get-words-from-files [files]
  (reduce
    (fn [coll file]
      (let [words (get-file-words file)]
        (text-frequency/merge-words coll words))) {} files))

(def MIN_WORD_FREQUENCY 10)

(defn -main [& args]
  (let [files (fs/get-files "./data/movies")
        words (get-words-from-files files)
        relevant-words (into {} (filter #(> (val %) MIN_WORD_FREQUENCY) words))
        sorted-words (text-frequency/sort-words relevant-words)
        to-text (reduce
                  (fn [text key-value] (str text "\n" (key key-value) ";" (val key-value)))
                  "" sorted-words)]
    (fs/save-words to-text)))




; (defn -main [& args]
;   (let [words (fs/get-file-lines "result.txt")
;         words-sum (counter/sum words)
;         words-filtered (filter #(> (second %) 50) words-sum)
;         words-sorted (sorter/sort-words (into {} words-filtered))
;         ws (take 1000 words-sorted)]
;     (fs/save-result ws)))



; (defn -main [& args]
;   (let [files (fs/get-files "./data/movies")]
;     (doseq [f files]
;       (let [lines (fs/get-file-lines f)
;             filtered-lines (filter-lines lines)
;             words (parser/get-words-from-lines filtered-lines)]
;           (fs/save-words (clojure.string/join "\n" words))))))

; (defn -main [& args]
;   (let [files (fs/get-files "./data/books")]
;     (doseq [f files]
;       (let [lines (fs/get-file-lines f)
;             words (parser/get-words-from-lines lines)]
;           (fs/save-words (clojure.string/join "\n" words))))))
