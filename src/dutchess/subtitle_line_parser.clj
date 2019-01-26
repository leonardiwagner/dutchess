(ns dutchess.subtitle-line-parser
  (:require [clojure.string :refer [lower-case]]))

(def subtitle-index-match #"^[0-9]+$")
(def subtitle-timestamp-match #"^[0-9]+.+\s-->\s+[0-9]+.*")
(def subtitle-text #".*[a-z|A-Z].*")

(defn parse [line]
  (if
    (and
      (not (re-matches subtitle-index-match line))
      (not (re-matches subtitle-timestamp-match line))
      (re-matches subtitle-text line))
    (spit "result.txt" (str "\n" (lower-case line)) :append true)))
