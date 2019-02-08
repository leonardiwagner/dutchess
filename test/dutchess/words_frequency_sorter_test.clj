(ns dutchess.words-frequency-sorter-test
  (:require [clojure.test :refer :all]
            [dutchess.words-frequency-sorter :as words-frequency-sorter]))

(deftest should-sort-word-frequency
  (let [words-frequency {:a 2 :b 1 :c 5 :d 4}
        result (words-frequency-sorter/sort-words words-frequency)
        expected-result {:c 5 :d 4 :a 2 :b 1}]
    ;TODO: it's not testing map order!
    (is (= result expected-result))))
