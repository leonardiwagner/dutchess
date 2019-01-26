(ns dutchess.subtitles-reader.files-reader
  (:require [clojure.java.io :as io]))

(defn get-directory-items [path]
  (let
    [directory (clojure.java.io/file path)
     files (.listFiles directory)]
    (map #(.getAbsolutePath %) files)))

(defn get-files-from-directories [directories]
  (reduce
    (fn [acc directory]
      (let [files (get-directory-items directory)]
        (concat acc files)))
    []
    directories))

(defn get-files-lines [files callback]
  (doseq [file files]
    (println (str "reading file " file))
    (with-open [rdr (clojure.java.io/reader file)]
      (line-seq rdr)
      (doseq [line (line-seq rdr)]
        (callback line)))))

(defn get-subtitles-lines [subtitles-path callback]
  (let [directories (get-directory-items subtitles-path)
        files (get-files-from-directories directories)]
    (get-files-lines files callback)))
