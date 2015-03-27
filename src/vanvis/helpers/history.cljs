(ns vanvis.helpers.history)

(defn push-stroke [path app]
  (let [new-history (conj (:history @app) path)]
    (swap! app assoc :history new-history)))

;; This is the history reset function.
;; Initialiatlly used as a propaganda tool,
;; this function has been reappropriated for
;; more productive use.
(defn burn-books [app]
  (swap! app assoc :history [{:x nil :y nil}]))

(defn stop-drag [app]
  (swap! app assoc :dragging false)
  (burn-books app))
