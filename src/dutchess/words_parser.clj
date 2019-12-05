(ns dutchess.words-parser)

(defn cleanup-line [line]
  (-> line
    (clojure.string/replace #"<[^>]*>" " ") ;remove html tags
    (clojure.string/replace #"[!|.|,|\"|?|:|(|)|…|;|*|=]" " "))) ;remove all non wanted chars

(defn cleanup-word [word]
  (-> word
    (clojure.string/trim)
    (clojure.string/lower-case)
    (clojure.string/replace #"^'|'$|^’|’$|^‘|‘$|^-|-$|^—|—$|^“|“$" "") ;remove unwanted chars around the word
    (clojure.string/replace #"‘|’" "'"))) ;padronize
    
(defn split-words [words]
  (reduce
    (fn [acc, word]
      (let [w (cleanup-word word)]
        (if (> (count w) 1)
          (conj acc w)
          acc)))
    []
    words))

(defn get-words-from-lines [lines]
  (reduce
    (fn [acc, line]
      (-> line
        (cleanup-line)
        (clojure.string/split #" ")
        (split-words)
        (->>
          (concat acc))))
    []
    lines))