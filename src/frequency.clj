#!/usr/bin/env bb

(require
   '[clojure.tools.cli :refer [parse-opts]]
   '[clojure.java.io :as io]
   '[clojure.string :as string])

(def progname "fequencies")

(def cli-options
  [["-f" "--file FILE" "File to process" :parse-fn #(slurp %)]
   ["-s" "--sort SORT" "Sort column" :validate [#(<= 1 % 2) :default 1]]
   ["-d" "--descending" "Sort in descending order"]
   ["-h" "--help"]])

(defn print-usage
  [progname summary]
  (println "usage: " progname " [opts]")
  (println " ")
  (println "options:")
  (println summary))

(defn find-errors
  [parsed]
  (let [{:keys [errors options]} parsed
        {:keys [help]} options]
    (cond
      help
      {:exit 0}

      errors
      {:message (string/join "\n" errors)
       :exit 1}
      )))

(defn print-errors
  [progname parsed errors]
  (let [{:keys [summary]} parsed
        {:keys [message exit]} errors]
    (when message
      (println message)
      (println " "))
    (print-usage progname summary)
    exit))


(defn format
  [data options]
  (do (println data)
      (println options)
    (reduce #(str %1 (first %2) "\t"  (second %2) "\n") "" data)))

(defn get-data
[options]
(or (:file options) *input*))


(let [parsed (parse-opts *command-line-args* cli-options)
      {:keys [options]} parsed]
  (or (some->> (find-errors parsed)
               (print-errors progname parsed)
               (System/exit))
      (-> (get-data options)
        (frequencies)
        (format options))))
