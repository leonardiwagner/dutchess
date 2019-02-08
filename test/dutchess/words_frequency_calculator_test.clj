(ns dutchess.words-frequency-calculator-test
  (:require [clojure.test :refer :all]
            [dutchess.words-frequency-calculator :as words-frequency-calculator]))

(deftest should-sort-word-frequency
  (let [words-map [{:a 1} {:b 1} {:a 1} {:a 1} {:c 1}]
        result (words-frequency-calculator/sum-words words-map)
        expected-result {:a 3 :b 1 :c 1}]
    (is (= result expected-result))))
