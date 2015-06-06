(ns pixel-mist.tools.color
  (:require [goog.dom :as dom]
            [pixel-mist.state :as app]
            [pixel-mist.helpers.history :as hist]))

(defn handle-event [evt]
  (let [key-code (.-keyCode evt)
        tar (.-target evt)
        color (.-value tar)]
    (if (= key-code 13) (hist/push-color color) nil)))

(defn render [el child]
  (let [color (:color @app/app-state)]
    (set! (.. el -style -background) color)))

;; Alfred style popup to input exact
;; hex values
(defn init-picker []
  (let [el (dom/createDom "input" (clj->js {:id "colorpicker" :type "text"}))
        cb handle-event
        picker {:el el
                :render render
                :cb cb }]
    (hist/push-overlay picker)))
