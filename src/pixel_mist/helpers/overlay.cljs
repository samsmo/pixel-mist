(ns pixel-mist.helpers.overlay
  (:require [pixel-mist.helpers.events :as events]
            [goog.dom :as dom]
            [goog.style :as style])
  (:require-macros [cljs.core.async.macros :refer [go]]))
;; Type: Generic Component
;;

;; generic events that will blindly
;; call it's child to do something~
;; parent (I HAVE SOMETHING FOR U) --> child (does dishes)
(defn bind-events [el cb]
  (let [kp (events/listen el "keypress")]
    (go (while true
          (let [kp (<! kp)]
            (cb kp))))))

(defn unbind-events [el]
  (events/unlisten el "keypress"))

(defn render [overlay]
  (let [{:keys [active child container]} overlay
        {:keys [el cb render]} child]
    (if active
      (do
        (dom/appendChild container el)
        (dom/appendChild (. js/document -body) container)
        (bind-events el cb)
        (render container el))
      (do
        (unbind-events el)
        (dom/removeChildren container)
        (dom/removeNode container)))))
