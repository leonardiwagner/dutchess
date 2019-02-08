(ns dutchess.words-map-parser)

(defn- word->word-map [word]
  {(keyword word) 1})

(defn- cleanup-word [word]
  (-> word
    (clojure.string/replace #"<[^>]*>" "")
    (clojure.string/replace #"[!|.|,|\"|?|:]" "")
    (clojure.string/trim)))

(defn words-coll->words-map [words]
  (reduce
    (fn [acc, word]
      (-> word
        (cleanup-word)
        (word->word-map)
        (->>
          (conj acc))))
    []
    words))
