(ns pixel-mist.tools.pencil)

(defn draw-pixel [coords app]
  (let [{:keys [color scale context]} @app
        {:keys [x y]} coords
        hist (conj (:history @app) {:prevX x :prevY y})]
    (set! (.-fillStyle context) color)
    (. context (fillRect x y scale scale))))


(defn render[opts app]
  (doseq [k opts]
    (if (not (nil? (:x k)))
      (draw-pixel k app)
     nil)))
