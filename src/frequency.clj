#!/usr/bin/env bb

(require
   '[clojure.tools.cli :refer [parse-opts]]
   '[clojure.java.io :as io])

(def progname "fequencies")

(def cli-options
  [["-f" "--file FILE" "File to process" :parse-fn #(slurp %)]
   ["-s" "--sort SORT" "Sort column" :validate [#(<= 1 % 2) :default 1]]
   ["-d" "--descending" "Sort in descending order"]
   ["-h" "--help"]])

(defn format
  [data options]
  (reduce #(str %1 "\t"  %2 "\n") result))

(defn get-data
[options]
(or (:file options) (*input*)))


(defn -main [& args]
  (let [parsed (parse-opts args cli-options)
        {:keys [options]} parsed]
    (or (some->> (opts/find-errors parsed)
                 (opts/print-errors progname parsed)
                 (System/exit))
        (-> (get-data options)
        	(format options)))))
