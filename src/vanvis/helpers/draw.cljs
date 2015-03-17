(ns vanvis.helpers.brush
  (:require [vanvis.helpers.math :as math]))

(defn fill-in-blanks [sX sY decX decY x y app fnc]
  (let [{:keys [scale context]} @app
        nextX (if (not= decX 0) (math/inc-pos-neg decX sX scale) x)
        nextY (if (not= decY 0) (math/inc-pos-neg decY sY scale) y)
        absX (. js/Math (abs (/ decX scale)))
        absY (. js/Math (abs (/ decY scale)))]
    (fnc nextX nextY app)
    (when (or (> absX 1) (> absY 1))
      (fill-in-blanks nextX nextY (math/decrement nextX x) (math/decrement nextY y) x y app fnc)
      )))

(defn draw [app fnc]
  (let [{:keys [scale context history]} @app
        {:keys [x y prevX prevY]} history
        x (* (. js/Math (ceil (/ x scale))) scale)
        y (* (. js/Math (ceil (/ y scale))) scale)
        xDist (- x prevX)
        yDist (- y prevY)]
    (when (and (not (nil? prevX))
               (or (> (. js/Math (abs xDist)) scale)
                   (> (. js/Math (abs yDist)) scale))
               (fill-in-blanks prevX prevY xDist yDist x y app fnc)))
    (fnc x y app)))
