(ns ^:figwheel-always vanvis.helpers)

(defn inc-pos-neg [check val incr]
  (if (> check 0)
    (+ val incr)
    (- val incr)))

(defn decrement [a b]
  (if (not= a b)
    (- b a)
    0))

(defn push-stroke [v app]
  (let [x (.-offsetX v)
        y (.-offsetY v)
        tool (:tool @app)
        nuHistory (conj (:history @app) {:x x :y y})]
    (swap! app assoc :history nuHistory)
    (tool app)))

;; This is the history reset function.
;; Initialiatlly used as a propaganda tool,
;; this function has been reappropriated for
;; more productive use.
(defn burn-books [app]
  (swap! app assoc :dragging false :history {:x nil :y nil}))
