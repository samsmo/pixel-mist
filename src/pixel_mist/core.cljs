(ns ^:figwheel-always pixel-mist.core
  (:require [cljs.core.async :as async
             :refer [<! >! chan put! timeout]]
            [goog.events :as events]
            [goog.dom :as dom]
            [pixel-mist.state :as app]
            [pixel-mist.helpers.history :as hist]
            [pixel-mist.helpers.brush :as brush]
            [pixel-mist.helpers.kboard :as kboard]
            [pixel-mist.tools.grid :as grid])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(enable-console-print!)

(defn canvas-adjust [context]
  (set! (.. context -canvas -width) (.-innerWidth js/window))
  (set! (.. context -canvas -height) (.-innerHeight js/window)))

(defn listen [el type]
  (let [c (chan)]
    (events/listen el type #(put! c %))
    c))

(defn bind-tooling-events [el out]
  (let [kp (listen (. js/document -body) "keypress")]
    (go (while true
          (let [key-event (<! kp)
                char-code (.-charCode key-event)]
            (kboard/press char-code app/app-state))))))

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
              mc ((let [pixel-strokes (brush/calc-path (. v -offsetX) (. v -offsetY))]
                    (hist/push-stroke pixel-strokes app/app-state))
                  (hist/stop-drag app/app-state))))))))

;; Faux render 'loop'
(add-watch app/app-state :render
           (fn [_ _ old new]
             (when (not= (get-in old [:history])
                         (get-in new [:history]))
               (let [tool (:tool @app/app-state)
                     newHist (get-in new [:history])]
                 (tool (last newHist) app/app-state)))
             (when (not= (get-in old [:grid])
                         (get-in new [:grid]))
               (let [coords (get-in new [:grid :coords])
                     grid-ctx (get-in new [:grid :ctx])]
                 (grid/render coords grid-ctx)))))

(defn setup []
  (let [draw-canvas (:canvas @app/app-state)
        draw-ctx (. draw-canvas (getContext "2d"))
        grid-canvas (get-in @app/app-state [:grid :canvas])
        grid-ctx (. grid-canvas (getContext "2d"))]
    (swap! app/app-state assoc :context draw-ctx)
    (swap! app/app-state assoc-in [:grid :ctx] grid-ctx)
    (dom/appendChild (. js/document -body) draw-canvas)
    (dom/appendChild (. js/document -body) grid-canvas)
    (canvas-adjust draw-ctx)
    (canvas-adjust grid-ctx)
    (bind-drawing-events draw-canvas draw-ctx)
    (bind-tooling-events draw-canvas draw-ctx)))

(setup)

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )
