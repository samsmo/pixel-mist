(ns ^:figwheel-always vanvis.core
  (:require [cljs.core.async :as async
             :refer [<! >! chan put! timeout]]
            [goog.events :as events]
            [goog.dom :as dom]
            [vanvis.state :as app]
            [vanvis.helpers :as helpers]
            [vanvis.tools.pencil :as pencil])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(enable-console-print!)

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
              md (swap! app/app-state assoc :dragging true)
              mv (when (:dragging @app/app-state)
                   (let [x (.-offsetX v)
                         y (.-offsetY v)
                         nuHistory (conj (:history @app/app-state) {:x x :y y})]
                     (swap! app/app-state assoc :history nuHistory)
                     (pencil/draw app/app-state)))
              mu (swap! app/app-state assoc :dragging false :history {:x nil :y nil})
              ))))))

(defn setup []
  (swap! app/app-state assoc :context (. (:canvas @app/app-state) (getContext "2d")))
  (dom/appendChild (. js/document -body) (:canvas @app/app-state))
  (let [context (:context @app/app-state)
        canvas (:canvas @app/app-state)]
    (canvas-adjust context)
    (bind-drawing-events canvas context)))

(setup)
