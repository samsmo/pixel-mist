(ns pixel-mist.helpers.history
  (:require [pixel-mist.state :as app]))

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

;; intermediary func
(defn push-grid [grid]
  (let [active (not (get-in @app/app-state [:grid :active]))
        grid (if (true? active) grid [{:x 0 :y 0 :type "horizontal"}])
        new-grid (update-in @app/app-state [:grid] assoc :active active :coords grid)]
    (swap! app/app-state assoc :grid (:grid new-grid))))

(defn push-scale [scale]
  (let [scale (if (> scale 0) scale 5)]
    (swap! app/app-state assoc :scale scale)))
