(ns vanvis.helpers.math)

(defn inc-pos-neg [check val incr]
  (if (> check 0)
    (+ val incr)
    (- val incr)))

(defn decrement [a b]
  (if (not= a b)
    (- b a)
    0))

;;----------------------
;; This function returns a pixelated
;; vector based on mouse events
(defn calc-path [evt]
  (let [x (.-offsetX evt)
        y (.-offsetY evt)
        path {}]
    path))
