(ns dutchess.word-definition.dictionary
  (require [net.cgrand.enlive-html :as html]))

(defn is-dutch-title-element? [element]
  (let [span (first (html/select element [:h2]))
        spanText (:id (:attrs (first (:content span))))]
    (if (and (not-empty spanText) (= "dutch" (clojure.string/lower-case spanText)))
      true
      false)))

(defn is-language-title-element? [element]
  (let [span (html/select element [:h2])]
    (not-empty span)))

(defn get-dutch-content [elements]
  (let [from-dutch-start (drop-while #(not (is-dutch-title-element? %)) elements)
        untill-dutch-end (take-while #(not (is-language-title-element? %)) (rest from-dutch-start))]
    untill-dutch-end))

(defn get-elements-with-definition [elements]
  (filter
    (fn [element]
      (cond
        (= (:tag element) :h3)
        true
        (= (:tag element) :h4)
        true
        :else false))
    elements))

(defn get-elements-definiton [elements]
  (map #(clojure.string/lower-case (first (:content (first (:content %))))) elements))

(defn get-official-definitions [definitions]
  (let [official-definitions ["pronoun", "verb", "noun", "adjective", "adverb", "conjunction", "preposition", "interjection", "letter", "numeral", "article", "particle", "mutation", "determiner", "participle", "circumposition"]]
    (reduce
      (fn [acc, definition]
        (if (some #{definition} official-definitions)
          (conj acc definition)
          acc))
      []
      definitions)))

(defn extract-word-info [html]
  (let [snippet (html/html-snippet html)
        elements (html/select snippet [:div.mw-parser-output])
        element (first elements)
        children (:content element)
        notEmptyChildren (filter #(not= (type %) java.lang.String) children)
        dutch-content (get-dutch-content notEmptyChildren)
        elementDefinitions (get-elements-with-definition dutch-content)
        definitions (get-elements-definiton elementDefinitions)]
    (get-official-definitions (distinct definitions))))

(defn read-word [word]
  (try
    (-> (str "https://en.wiktionary.org/wiki/" word)
      (slurp)
      (extract-word-info))
    (catch Exception e (println (.toString e)))))
