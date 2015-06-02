(ns pixel-mist.tools.grid
  (:require [goog.dom :as dom]
            [pixel-mist.state :as app]))

(defn draw-grid [])

(defn render []
  (draw-grid))

;; calculation method
;; possible not the place for it
;; not really sure where else this
;; should go for now..

(defn calc-grid []
  (let [{:keys [scale]} @app/app-state
        maxX (/ (.-innerWidth js/window) scale)
        maxY (/ (.-innerHeight js/window) scale)
        path {:x (take maxX (iterate (partial + scale) 0))
              :y (take maxY (iterate (partial + scale) 0))}]
    path))
