(ns dutchess.file-system
  (:require [clojure.java.io :as io]))

(defn get-files [path]
  (-> path
    (clojure.java.io/file)
    (.listFiles)
    (->>
      (map #(.getAbsolutePath %)))))

(defn get-file-lines [file]
  (with-open [reader (clojure.java.io/reader file)]
    (-> 
      (line-seq reader)
      (doall))))


(defn save-words [text]
  (spit "result.txt" text :append true :encoding "UTF-8"))
