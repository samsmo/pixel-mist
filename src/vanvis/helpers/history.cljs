(ns vanvis.helpers.history)

(defn push-stroke [v app]
  (let [x (.-offsetX v)
        y (.-offsetY v)
        tool (:tool @app)
        nuHistory (conj (:history @app) {:x x :y y})]
    (swap! app assoc :history nuHistory)
    (tool app)))

;; This is the history reset function.
;; Initialiatlly used as a propaganda tool,
;; this function has been reappropriated for
;; more productive use.
(defn burn-books [app]
  (swap! app assoc :dragging false :history {:x nil :y nil}))
