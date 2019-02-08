(ns dutchess.data-reader-test
  (:require [clojure.test :refer :all]
            [dutchess.data-reader :as data-reader]))

(deftest should-read-books
  (let [result (data-reader/read-books-words "data-example/books")]
    (is (= (count result) 8))))

(deftest should-read-subtitles
  (let [result (data-reader/read-subtitles-words "data-example/subtitles")]
    (is (= (count result) 16))))
