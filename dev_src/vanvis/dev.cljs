(ns vanvis.dev
    (:require
     [vanvis.core]
     [figwheel.client :as fw]))

(vanvis.core/setup)

(fw/start {
  :websocket-url "ws://localhost:3449/figwheel-ws"
  :on-jsload (fn []
               ;; (stop-and-start-my app)
               )})
