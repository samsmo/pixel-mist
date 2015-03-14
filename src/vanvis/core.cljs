(ns ^:figwheel-always vanvis.core
  (:require [cljs.core.async :as async
             :refer [<! >! chan put! timeout]]
            [goog.events :as events]
            [goog.dom :as dom])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(enable-console-print!)

;let's set up some defaults yao!
(defonce app-state (atom {
                          :tool :pencil
                          :dragging false
                          :color "#FF33CC"
                          :scale 5
                          :history {:prevX nil :prevY nil}
                          :canvas (dom/createDom "canvas")
                          :context nil
                          }))

(defn draw-pixel [x y context]
  (let [scale (:scale @app-state)
        color (:color @app-state)
        hist (conj (:history @app-state) {:prevX x :prevY y})]
    (set! (.-fillStyle context) color)
    (. context (fillRect x y scale scale))
    (swap! app-state assoc :history hist)))

(defn inc-pos-neg [check val incr]
  (if (> check 0)
    (+ val incr)
    (- val incr)))

(defn decrement [a b]
  (if (not= a b)
    (- b a)
    0))

(defn fill-in-blanks [sX sY decX decY x y context]
  (let [scale (:scale @app-state)
        nextX (if (not= decX 0) (inc-pos-neg decX sX scale) x)
        nextY (if (not= decY 0) (inc-pos-neg decY sY scale) y)
        absX (. js/Math (abs (/ decX scale)))
        absY (. js/Math (abs (/ decY scale)))]
    (draw-pixel nextX nextY context)
    (when (or (> absX 1) (> absY 1))
      (fill-in-blanks nextX nextY (decrement nextX x) (decrement nextY y) x y context)
      )))

(defn draw [x y context {:keys [prevX prevY]}]
  (let [scale (:scale @app-state)
        x (* (. js/Math (ceil (/ x scale))) scale)
        y (* (. js/Math (ceil (/ y scale))) scale)
        xDist (- x prevX)
        yDist (- y prevY)]
    (when (and (not (nil? prevX))
               (or (> (. js/Math (abs xDist)) scale)
                   (> (. js/Math (abs yDist)) scale))
               (fill-in-blanks prevX prevY xDist yDist x y context)))
    (draw-pixel x y context)))

(defn canvas-adjust [context]
  (set! (.. context -canvas -width) (.-innerWidth js/window))
  (set! (.. context -canvas -height) (.-innerHeight js/window)))

(defn listen [el type]
  (let [c (chan)]
    (events/listen el type #(put! c %))
    c))

(defn bind-drawing-events [el out]
  (let [mv (listen el "mousemove")
        md (listen el "mousedown")
        mu (listen el "mouseup")
        msg :draw-end]
    (go (while true
          (let [[v d] (alts! [mv md mu])]
            (condp = d
              md (swap! app-state assoc :dragging true)
              mv (when (:dragging @app-state)
                   (draw (.-offsetX v) (.-offsetY v) out (:history @app-state)))
              mu (swap! app-state assoc :dragging false :history {:x nil :y nil})
              ))))))

(defn setup []
  (swap! app-state assoc :context (. (:canvas @app-state) (getContext "2d")))
  (dom/appendChild (. js/document -body) (:canvas @app-state))
  (let [context (:context @app-state)
        canvas (:canvas @app-state)]
    (canvas-adjust context)
    (bind-drawing-events canvas context)))

(setup)
