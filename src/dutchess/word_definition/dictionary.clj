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

(defn get-element-definition [element]
  (cond
    (= (:tag element) :h3)
    (let [text (first (:content (first (:content element))))]
      text)
    (= (:tag element) :h4)
    (let [text (first (:content (first (:content element))))]
      text)))

(defn extract-word-info [html]
  (let [snippet (html/html-snippet html)
        elements (html/select snippet [:div.mw-parser-output])
        element (first elements)
        children (:content element)
        notEmptyChildren (filter #(not= (type %) java.lang.String) children)
        dutch-content (get-dutch-content notEmptyChildren)]
    (doseq [x dutch-content]
      (println (get-element-definition x)))))
    ;(println (map #(get-element-definition %) dutch-content))))

(defn read-word [word]
  (-> (str "https://en.wiktionary.org/wiki/" word)
    (slurp)
    (extract-word-info)))
    ;(hickory.core/as-hiccup)
    ;(extract-word-info)))
