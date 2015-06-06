(ns pixel-mist.helpers.events
  (:require [cljs.core.async :as async
             :refer [<! >! chan put! timeout]]
            [goog.events :as g-events]))

(defn listen [el type]
  (let [c (chan)]
    (g-events/listen el type #(put! c %))
    c))

(defn unlisten [el type]
  (g-events/removeAll el type))
