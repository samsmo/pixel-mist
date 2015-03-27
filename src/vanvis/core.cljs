(ns ^:figwheel-always vanvis.core
  (:require [cljs.core.async :as async
             :refer [<! >! chan put! timeout]]
            [goog.events :as events]
            [goog.dom :as dom]
            [vanvis.state :as app]
            [vanvis.helpers.history :as hist]
            [vanvis.helpers.brush :as brush])
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
        mc (listen el "click")
        msg :draw-end]
    (go (while true
          (let [[v d] (alts! [mv md mu mc])]
            (condp = d
              md (swap! app/app-state assoc :dragging true)
              mv (when (:dragging @app/app-state)
                   (let [pixel-strokes (brush/calc-path (. v -offsetX) (. v -offsetY))]
                     (hist/push-stroke pixel-strokes app/app-state)))
              mu (hist/stop-drag app/app-state)
              mc (println "lik")
              ;mc ((hist/stop-drag app/app-state))
              ))))))

;; Faux render 'loop'
(add-watch app/app-state :render
           (fn [_ _ old new]
             (when (not= (get-in old [:history])
                         (get-in new [:history]))
               (let [tool (:tool @app/app-state)
                     newHist (get-in new [:history])]
                     (tool (last newHist) app/app-state)
                 ))))

(defn setup []
  (swap! app/app-state assoc :context (. (:canvas @app/app-state) (getContext "2d")))
  (dom/appendChild (. js/document -body) (:canvas @app/app-state))
  (let [context (:context @app/app-state)
        canvas (:canvas @app/app-state)]
    (canvas-adjust context)
    (bind-drawing-events canvas context)))

