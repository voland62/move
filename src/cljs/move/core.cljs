(ns move.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
   [cljs.core.async :as async :refer [>! <! put! take! chan alts! timeout dropping-buffer]]
   [goog.events :as events]
   [move.time :as t ]
   [clojure.core.reducers :as r])
  (:import [goog.events EventType]))

(def aaa 23)


(defn foo [] (println "Hello from move.core namespace!"))

;;--- time model ---------------------------------------
(defonce my-time (t/create-time-model-one true))



;;--- domain model -------------------------------------
(defonce my-obj (atom {a: nil b: nil foo: nil}))

(defn set-domane-state [ {:keys [a b foo]} ]
  ())




;;--- business stuff -------------------------------------
(defn draw-circle [x y r]
  (let [canvas (.getElementById js/document "canvas")
        cxt (.getContext canvas "2d")]
    (doto cxt
      .beginPath
      (aset "fillStyle" "green")
      (.arc x y r 0 (* 2 js/Math.PI))
      .fill)))

(defn clean-context []
  (let [canvas (.getElementById js/document "canvas")
        cxt (.getContext canvas "2d")
        w (.-width canvas)
        h (.-height canvas)]
    (.clearRect cxt 0 0 w h)))

;;--- utils --------------------------------------------------------------


;;--- pause/run processing ------------------------------
(defn create-key-chan
  []
  (let [chan-transducer (comp (map #(.-keyCode %))
                               (filter #{37 39})
                               (map {37 :<- 39 :->}))
        c (chan 1 chan-transducer)]
    (events/listen js/window EventType.KEYDOWN
                   (fn [e] (put! c e)))
    c))

;;--- start simulation ---------------------------------------------------

(defonce key-chan (create-key-chan))

(declare do-job)

(defn start-simulation []
  (go
    (loop []
      (let [v (<! key-chan)]
        (do-job v)
        (recur)))))

(defn do-job [val]
  ;(.log js/console (str "from chanel:" val ))


  )



;; --- chestnut default generation. Can be removed ------------------------
;;(defonce app-state (atom {:text "Hello Chestnut!"}))

(defn main [] (println "Hello Andrew"))

#_(defn main []
  (om/root
    (fn [app owner]
      (reify
        om/IRender
        (render [_]
          (dom/h1 nil (:text app)))))
    app-state
    {:target (. js/document (getElementById "app"))}))
