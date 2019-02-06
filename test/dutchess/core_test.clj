(ns dutchess.core-test
  (:require [clojure.test :refer :all]
            [dutchess.words-together :as words-together]))

(def words ["a" "b" "c" "d" "e" "a" "b" "c" "i" "j" "a" "b" "c" "k" "w"])
(def counter {:2 {} :3 {} :4 {} :5 {}})

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
    [5 4 3 2]))

(defn x [words]
  (reduce
    (fn [acc, word]
      (let [updated-words (conj (:words acc) word)]
        (if (>= (count (:words acc)) 5)
          (let [updated-counter (update-counter acc)]
            (assoc acc :words updated-words :counter updated-counter))
          (assoc acc :words updated-words))))
    {:words [] :counter counter}
    words))

(deftest a-test
  (testing "FIXME, I fail."
    (println (x words))))
