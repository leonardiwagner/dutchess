(ns dutchess.main
  (:require [dutchess.file-system :as fs])
  (:require [dutchess.words-counter :as counter])
  (:require [dutchess.words-sorter :as sorter])
  (:require [dutchess.words-parser :as parser]))

(defn -main [& args]
  (let [words (fs/get-file-lines "result.txt")
        words-sum (counter/sum words)
        words-filtered (filter #(> (second %) 500) words-sum)
        words-sorted (sorter/sort-words (into {} words-filtered))
        ws (take 100 words-sorted)]
    (doseq [x ws]
      (println x))))





(defn filter-lines [lines]
  (let [subtitle-index-match #"^[0-9]+$"
        subtitle-timestamp-match #"^[0-9]+.+\s-->\s+[0-9]+.*"
        subtitle-text #".*[a-z|A-Z].*"]
    (reduce
      (fn [acc line]
        (if
          (and
            (not (re-matches subtitle-index-match line))
            (not (re-matches subtitle-timestamp-match line))
            (re-matches subtitle-text line))
          (conj acc line)
          acc))
      []
      lines)))

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
