(ns vanvis.tools.pencil
  (:require [vanvis.helpers :as helpers]))

(defn draw-pixel [x y app]
  (let [scale (:scale @app)
        color (:color @app)
        context (:context @app)
        hist (conj (:history @app) {:prevX x :prevY y})]
    (set! (.-fillStyle context) color)
    (. context (fillRect x y scale scale))
    (swap! app assoc :history hist)))

(defn fill-in-blanks [sX sY decX decY x y app]
  (let [{:keys [scale context]} @app
        nextX (if (not= decX 0) (helpers/inc-pos-neg decX sX scale) x)
        nextY (if (not= decY 0) (helpers/inc-pos-neg decY sY scale) y)
        absX (. js/Math (abs (/ decX scale)))
        absY (. js/Math (abs (/ decY scale)))]
    (draw-pixel nextX nextY app)
    (when (or (> absX 1) (> absY 1))
      (fill-in-blanks nextX nextY (helpers/decrement nextX x) (helpers/decrement nextY y) x y app)
      )))

(defn draw [app]
  (let [{:keys [scale context history]} @app
        {:keys [x y prevX prevY]} history
        x (* (. js/Math (ceil (/ x scale))) scale)
        y (* (. js/Math (ceil (/ y scale))) scale)
        xDist (- x prevX)
        yDist (- y prevY)]
    (when (and (not (nil? prevX))
               (or (> (. js/Math (abs xDist)) scale)
                   (> (. js/Math (abs yDist)) scale))
               (fill-in-blanks prevX prevY xDist yDist x y app)))
    (draw-pixel x y app)))

