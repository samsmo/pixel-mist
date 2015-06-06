(ns pixel-mist.state
  (:require [goog.dom :as dom]
            [pixel-mist.tools.pencil :as pencil]))

;let's set up some defaults yao!
(defonce app-state (atom {
                          :tool pencil/render
                          :dragging false
                          :color "#FF33CC"
                          :scale 10
                          :history []
                          :canvas (dom/createDom "canvas")
                          :context nil
                          :overlay {
                                  :active false
                                  :container (dom/createDom "section" (clj->js {:id "overlay"}))
                                  :child {
                                          :el nil
                                          :cb nil
                                          }
                                  }
                          :grid {
                                 :active false
                                 :canvas (dom/createDom "canvas" (clj->js {:id "grid"}))
                                 :ctx nil
                                 :coords {:x 0 :y 0}
                                 }
                          }))
