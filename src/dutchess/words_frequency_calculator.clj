(ns dutchess.words-frequency-calculator)

(defn sum-words [words-map]
  (reduce
    (fn [acc word-map]
      (let [key-word (first (keys word-map))]
        (if (contains? acc key-word)
          (assoc acc key-word (inc (get acc key-word)))
          (assoc acc key-word 1))))
    {}
    words-map))
