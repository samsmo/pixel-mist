(ns vanvis.state
  (:require [goog.dom :as dom]
            [vanvis.tools.pencil :as pencil]))

;let's set up some defaults yao!
(defonce app-state (atom {
                          :tool pencil/render
                          :dragging false
                          :color "#FF33CC"
                          :scale 5
                          :history []
                          :canvas (dom/createDom "canvas")
                          :context nil
                          }))
