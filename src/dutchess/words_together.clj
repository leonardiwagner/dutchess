(ns dutchess.words-together)

(def counter {:2 {} :3 {} :4 {} :5 {} :6 {}})

(defn count-words [words counter]
  (println words)
  counter)

(defn update-counter [{words :words, counter :counter}]
  (reduce
    (fn [counter-acc, i]
      (let [n-words (take-last i words)
            n-key (keyword (str i))
            words-key (keyword (clojure.string/join "-" n-words))]
        (if (contains? (n-key counter-acc) words-key)
          (assoc-in counter-acc [n-key words-key] (inc (get-in counter-acc [n-key words-key])))
          (assoc-in counter-acc [n-key words-key] 1))))
    counter
    [3]))

(defn count-words-together [words]
  (reduce
    (fn [acc, word]
      (print (str "\r" (count (:words acc)) " of " (count words)))
      (let [updated-words (conj (:words acc) word)]
        (if (>= (count (:words acc)) 5)
          (let [updated-counter (update-counter acc)]
            (assoc acc :words updated-words :counter updated-counter))
          (assoc acc :words updated-words))))
    {:words [] :counter counter}
    words))

(defn remove-low-usage-values [counter]
  (reduce-kv
    (fn [m, k v]
      (if (> v 10)
        (assoc m k v)
        m))
    {}
    counter))
(defn get [file]
  (with-open [rdr (clojure.java.io/reader file)]
    (let [words (take 20000 (line-seq rdr))
          result (:counter (count-words-together words))]
         (spit
           "together.txt"
           (map
             (fn [[k v]]
               (remove-low-usage-values v))
             result)))))

; (defn count-words [coll words]
;   (let [keyword-words (keyword words)]
;     (if (contains? coll keyword-words)
;       (assoc coll keyword-words (inc (keyword-words coll)))
;       (assoc coll keyword-words 1))))
;
; ; (let [word-count (- 5 i)
; ;       words (take word-count words)
; ;       counter-key (keyword (str word-count))
; ;       counter-coll (get counter counter-key)
; ;       new-coll (count-words counter-coll words)]
; ;   (assoc counter counter-key new-coll)))))
;
; (defn count-words-group [acc]
;   (println (:counter acc))
;   (map
;     (fn [c]
;       (println c))
;     (keys (:counter acc))))
;
; (defn get [file]
;   (with-open [rdr (clojure.java.io/reader file)]
;     (let [words (take 50 (line-seq rdr))
;           counter {:2 {} :3 {} :4 {} :5 {}}]
;       (reduce
;         (fn [acc, word]
;           (if (>= (count acc) 5)
;             (let [updated-counter (count-words-group acc)])
;             (println (count-words-group acc)))
;           (conj acc word))
;         {:words [] :counter counter}
;         words))))
