(ns vanvis.state
  (:require [goog.dom :as dom]))

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
