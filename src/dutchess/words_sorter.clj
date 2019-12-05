(ns dutchess.words-sorter)

(defn sort-words [words-map]
  (into
    (sorted-map-by
      (fn [key1 key2]
        (let [val1 (words-map key1)
              val2 (words-map key2)]
          (cond
            (= val1 val2) (.compareTo key2 key1)
            (< val1 val2) 1
            :else -1))))
    words-map))