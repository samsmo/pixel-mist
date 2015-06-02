(ns pixel-mist.helpers.kboard
  (:require [pixel-mist.tools.grid :as grid]
            [pixel-mist.helpers.history :as hist]))

(defn press [pressed app]
  (case pressed
    103 (hist/push-grid (grid/calc-grid))
    nil))
