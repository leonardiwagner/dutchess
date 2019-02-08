(ns dutchess.subtitle-line-parser)

(def ^:private subtitle-index-match #"^[0-9]+$")
(def ^:private subtitle-timestamp-match #"^[0-9]+.+\s-->\s+[0-9]+.*")
(def ^:private subtitle-text #".*[a-z|A-Z].*")

(defn subtitle-line? [line]
  (if
    (and
      (not (re-matches subtitle-index-match line))
      (not (re-matches subtitle-timestamp-match line))
      (re-matches subtitle-text line))
    true
    false))
