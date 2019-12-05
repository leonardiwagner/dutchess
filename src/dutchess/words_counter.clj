(ns dutchess.words-counter)

(defn sum [words]
  (reduce
    (fn [acc word]
      (let [key (keyword word)]
        (if (contains? acc key)
          (assoc acc key (inc (get acc key)))
          (assoc acc key 1))))
    {}
    words))


