(ns pixel-mist.tools.grid
  (:require [goog.dom :as dom]
            [pixel-mist.state :as app]))

(defn draw-line [grid ctx]
  (let [maxX (.-innerWidth js/window)
        maxY (.-innerHeight js/window)
        {:keys [x y type]} grid
        lineX (if (or (> x 0) (identical? type "horizontal")) x maxX)
        lineY (if (or (> y 0) (identical? type "vertical")) y maxY)]
    (. ctx (moveTo x y))
    (. ctx (lineTo lineX lineY))))

(defn clear-canvas [ctx width height]
  (.save ctx)
  (.setTransform ctx 1 0 0 1 0 0)
  (.clearRect ctx 0 0 width height)
  (.restore ctx))

(defn grid-starter [coords ctx]
  (. ctx (beginPath))
  (doseq [k coords] (draw-line k ctx))
  (set! (.-strokeStyle ctx) "#CCC")
  (set! (.-lineWidth ctx) 1)
  (. ctx (stroke))
  (. ctx (closePath)))

(defn render [grid]
  (let [{:keys [coords canvas ctx active]} grid]
    (clear-canvas ctx (.-width canvas) (.-height canvas))
    (if (true? active) (grid-starter coords ctx) nil)))

;; calculation method
;; possible not the place for it
;; not really sure where else this
;; should go for now..

(defn calc-grid []
  (let [{:keys [scale]} @app/app-state
        maxX (.-innerWidth js/window)
        maxY (.-innerHeight js/window)
        lazyX (range 0 maxX scale)
        lazyY (range 0 maxY scale)
        x (map #(hash-map :x % :y 0 :type "horizontal") lazyX)
        y (map #(hash-map :x 0 :y % :type "vertical") lazyY)
        path (concat y x)]
    path))
