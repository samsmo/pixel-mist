(ns pixel-mist.helpers.kboard
  (:require [pixel-mist.tools.grid :as grid]))

(defn press [e]
  (case e
    103 (grid/render)
    nil))
