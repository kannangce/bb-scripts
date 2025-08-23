#!/usr/bin/env bb

(require
  '[clojure.tools.cli :refer [parse-opts]]
  '[clojure.string :as str])

(def progname "fequencies")
(def ^:dynamic *debug* true)

(def cli-options
  [["-f" "--file FILE" "File to process" :parse-fn #(slurp %)]
   ["-s" "--sort" "Sort results based on frequency"]
   ["-d" "--descending" "Sort in descending order"]
   ["-h" "--help"]
   ["-D" "--debug"]])

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
             {:message (str/join "\n" errors)
              :exit    1}
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

(defn format [data]
      (->> data
           (map (fn [[k v]] (str k "\t" v)))
           (str/join "\n")))

(defn sort
      [data options]
      (if (:sort options)
        (let [sort-fn (if (:descending options) > <)]
             (sort-by val sort-fn data))
        data))

(defn get-data
      [options]
      (if (:file options)
        (str/split (:file options) #"\n")
        (str/split-lines (slurp *in*))))

(defn debug
      ([dat msg]
       (when (true? *debug*)
             (println "**Debug**" msg)
             (println dat)
             (println "*******"))
       dat)
      ([dat]
       (debug dat "")))

(let [parsed (parse-opts *command-line-args* cli-options)
      {:keys [options]} parsed]
     (or (some->> (find-errors parsed)
                  (print-errors progname parsed)
                  (System/exit))
         (binding [*debug* (:debug options)]
                  (-> (get-data options)
                      (frequencies)
                      (debug "frequency")
                      (sort options)
                      (format)
                      (print)))))
