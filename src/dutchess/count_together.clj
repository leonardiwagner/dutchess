(ns dutchess.count-together)

(defn count-words [wordLines]
  (reduce
    (fn [acc, word]
      (let [keywordWord (keyword word)]
        (if (contains? acc keywordWord)
          (assoc acc keywordWord (inc (keywordWord acc)))
          (assoc acc keywordWord 1))))
    {}
    wordLines

(defn sort-map-by-value [super-map]
  (into (sorted-map-by (fn [key1 key2] (let [val1 (super-map key1) val2 (super-map key2)] (cond (= val1 val2) (.compareTo key2 key1) (< val1 val2) 1 :else -1)))) super-map))

(defn save-line [line]
  (spit "together-count.txt" (str line "\n") :append true))

(defn count-together [file]
  (with-open [rdr (clojure.java.io/reader file)]
    (let [wordLines (line-seq rdr)
          wordMaps (count-words wordLines)
          sortedWordMaps (sort-map-by-value wordMaps)]
          ;words (seq sortedWordMaps)]
      (doseq [word sortedWordMaps]
        (save-line (str (val word) " " (key word)))))))
