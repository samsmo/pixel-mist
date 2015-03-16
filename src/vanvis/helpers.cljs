(ns ^:figwheel-always vanvis.helpers)

(defn inc-pos-neg [check val incr]
  (if (> check 0)
    (+ val incr)
    (- val incr)))

(defn decrement [a b]
  (if (not= a b)
    (- b a)
    0))
