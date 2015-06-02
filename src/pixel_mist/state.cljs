(ns pixel-mist.state
  (:require [goog.dom :as dom]
            [pixel-mist.tools.pencil :as pencil]))

;let's set up some defaults yao!
(defonce app-state (atom {
                          :tool pencil/render
                          :dragging false
                          :color "#FF33CC"
                          :scale 5
                          :history []
                          :canvas (dom/createDom "canvas")
                          :context nil
                          :grid {
                                 :canvas (dom/createDom "canvas")
                                 :coords {:x 0 :y 0}
                                 }
                          }))
