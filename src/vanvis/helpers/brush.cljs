(ns vanvis.helpers.brush
  (:require [vanvis.helpers.math :as math]
            [vanvis.state :as app]))

(defn predict-path [opts path]
  (let [{:keys [scale]} @app/app-state
        {:keys [prev-x prev-y x-dist y-dist x y]} opts
        next-x (if (not= x-dist 0) (math/inc-pos-neg x-dist prev-x scale) x)
        next-y (if (not= y-dist 0) (math/inc-pos-neg y-dist prev-y scale) y)
        abs-x (. js/Math (abs (/ x-dist scale)))
        abs-y (. js/Math (abs (/ y-dist scale)))
        opts {:prev-x next-x
               :prev-y next-y
               :x-dist (math/decrement next-x x)
               :y-dist (math/decrement next-y y)
               :x x
               :y y}
        mush (conj path {:x next-x :y next-y})]
    (if (or (> abs-x 1) (> abs-y 1))
      (predict-path opts mush)
      mush)))

(defn missing-pieces? [prev x y path]
  (let [{prev-x :x, prev-y :y} prev
        {:keys [scale]} @app/app-state
        x-dist (- x prev-x)
        y-dist (- y prev-y)
        opts {:prev-x prev-x
               :prev-y prev-y
               :x-dist x-dist
               :y-dist y-dist
               :x x
               :y y}]
    (if (and (not (nil? prev-x))
                 (or (> (. js/Math (abs x-dist)) scale)
                     (> (. js/Math (abs y-dist)) scale)))
      (predict-path opts path)
      path)))

(defn calc-path [x y]
  (let [{:keys [scale context history]} @app/app-state
        prev (last history)
        x (* (. js/Math (ceil (/ x scale))) scale)
        y (* (. js/Math (ceil (/ y scale))) scale)
        path (missing-pieces? (last prev) x y [{:x x :y y}])]
        path))
