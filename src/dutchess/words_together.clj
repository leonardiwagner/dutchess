(ns dutchess.words-together)

(def counter {:2 {} :3 {} :4 {} :5 {} :6 {} :7 {} :8 {}})

(defn update-counter [{words :words, counter :counter}]
  (reduce
    (fn [counter-acc, i]
      (let [n-words (reverse (take-last i words))
            n-key (keyword (str i))
            words-key (keyword (clojure.string/join "-" n-words))]
        (if (contains? (n-key counter-acc) words-key)
          (assoc-in counter-acc [n-key words-key] (inc (get-in counter-acc [n-key words-key])))
          (assoc-in counter-acc [n-key words-key] 1))))
    counter
    [2 3 4])) ; place here words length to be captured, more than 3 causes too much memory consumption: 8gb

(defn count-words-together [words]
  (let [words-count (count words)]
    (reduce
      (fn [acc, word]
        (print (str "\r" (:i acc)) " of " words-count)
        (let [updated-words (take 10 (conj (:words acc) word))
              updated-counter (update-counter acc)]
          (assoc acc :words updated-words :counter updated-counter :i (inc (:i acc)))))
      {:words ["a" "b" "c" "d" "e" "f" "g" "h" "i"] :counter counter :i 0}
      words)))

(defn remove-low-usage-values [counter]
  (reduce-kv
    (fn [m, k v]
      (if (> v 10)
        (assoc m k v)
        m))
    {}
    counter))

(defn clear [result]
  (reduce-kv
    (fn [m k v]
      (assoc m k (remove-low-usage-values v)))
    {}
    result))

(defn sort-map-by-value [super-map]
  (into (sorted-map-by (fn [key1 key2] (let [val1 (super-map key1) val2 (super-map key2)] (cond (= val1 val2) (.compareTo key2 key1) (< val1 val2) 1 :else -1)))) super-map))

(defn sort-n-print [filename values]
  (let [sorted-values (sort-map-by-value values)]
    (doseq [[k v] sorted-values]
      (spit filename (str v " " (clojure.string/replace k #"-" " ") "\n") :append true))))

(defn get [file]
  (with-open [rdr (clojure.java.io/reader file)]
    (let [words (line-seq rdr)
          raw (:counter (count-words-together words))
          result (clear raw)]
         (doseq [[k v] result]
           (sort-n-print (str "together_" k ".txt") v)))))
