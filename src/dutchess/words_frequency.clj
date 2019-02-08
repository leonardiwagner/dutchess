(ns dutchess.words-frequency
  (:require [dutchess.data-reader :as data-reader])
  (:require [dutchess.words-frequency-calculator :as words-frequency-calculator])
  (:require [dutchess.words-frequency-sorter :as words-frequency-sorter])
  (:require [dutchess.word-definition.dictionary] :as dictionary))
(def ^private top-words-count 500)

(defn get-frequency [path]
  (-> path
    (do
      (println "[1/4] Reading files...")
      (data-reader/read-books-words))
    (do
      (println "[2/4] Calculating words...")
      (words-frequency-calculator/sum-words))
    (do
      (println "[3/4] Sorting most used words...")
      (words-frequency-sorter/sort-words))
    (->>
      (take top-words-count)
      (-> ))))
