(ns dutchess.words-map-parser-test
  (:require [clojure.test :refer :all]
            [dutchess.words-map-parser :as words-map-parser]))

(deftest should-parse-words-to-maps
  (let [words ["a-ha" "test!" "<h1>word</h1>"]
        result (words-map-parser/words-coll->words-map words)
        expected-result [{:a-ha 1} {:test 1} {:word 1}]]
    (is (= result expected-result))))
