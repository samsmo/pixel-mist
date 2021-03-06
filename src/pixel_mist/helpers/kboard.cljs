(ns pixel-mist.helpers.kboard
  (:require [pixel-mist.tools.grid :as grid]
            [pixel-mist.helpers.history :as hist]
            [pixel-mist.state :as app]
            [pixel-mist.tools.color :as color]))

;; KEY KEY
;; 67 [c] change color
;; 71 [g] Toggle Grid
;; 187 [+] Increase Brush Size
;; 189 [-] Decrease Brush Size

(defn press [pressed app]
  (println pressed)
  (case pressed
    67 (color/init-picker)
    71 (hist/push-grid (grid/calc-grid))
    187 (hist/push-scale (+ (:scale @app/app-state) 5))
    189 (hist/push-scale (- (:scale @app/app-state) 5))
    nil))
