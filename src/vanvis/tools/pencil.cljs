(ns vanvis.tools.pencil
  (:require [vanvis.helpers.brush :as brush]))

(defn draw-pixel [x y app]
  (let [{:keys [color scale context]} @app
        hist (conj (:history @app) {:prevX x :prevY y})]
    (set! (.-fillStyle context) color)
    (. context (fillRect x y scale scale))
    (swap! app assoc :history hist)))

(defn draw [opts app]
  ;; loop through items
  ;; -> call draw pixel
  (println opts))


(defn handle-event[opts app]
  (draw opts app))
