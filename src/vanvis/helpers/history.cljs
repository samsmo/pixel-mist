(ns vanvis.helpers.history)

(defn push-stroke [v app]
  (let [        nuHistory (conj (:history @app) {:x x :y y})]
    (swap! app assoc :history nuHistory)))

;; This is the history reset function.
;; Initialiatlly used as a propaganda tool,
;; this function has been reappropriated for
;; more productive use.
(defn burn-books [app]
  (swap! app assoc  :history [{:x nil :y nil}]))

(defn stop-drag [app]
  (swap! app assoc :dragging false))
