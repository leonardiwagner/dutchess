(ns dutchess.word-definition.word-definition-finder
  (:require [clojure.java.io :as io])
  (:require [dutchess.word-definition.dictionary :refer [read-word]]))

(defn write-definition [line]
  (let [[count word0] (clojure.string/split line #" ")
        word (clojure.string/trim (clojure.string/replace word0 #":" ""))
        definitions (read-word word)
        line-to-write (str count ";" word ";" (clojure.string/join "" (map #(str "[" % "]") definitions)) ";" (clojure.string/join ";" definitions) "\n")]
    (spit "definitions.txt" line-to-write :append true :encoding "UTF-8")
    (println (str "got definition of the word: " word))))

(defn get-by-file [file]
  (with-open [rdr (clojure.java.io/reader file)]
    (doseq [line (take 1000 (line-seq rdr))]
      (write-definition line))))
