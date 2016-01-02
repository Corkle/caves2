(ns caves2.core
  (:require [play-clj.core :refer :all]
            [play-clj.ui :refer :all]))

(declare caves2-game title-screen main-screen debug-screen)

(defscreen title-screen
  :on-show
  (fn [screen entities]
    (update! screen :renderer (stage) :camera (orthographic))
    (assoc (label "Welcome to the Caves of Clojure!" (color :white)) :x 300 :y 400))

  :on-render
  (fn [screen entities]
    (clear!)
    (render! screen entities))

  :on-key-down
  (fn [screen entities]
    (cond
      (= (:key screen) (key-code :space)) (on-gl (set-screen! caves2-game main-screen debug-screen))
      (= (:key screen) (key-code :f5)) (on-gl (set-screen! caves2-game title-screen debug-screen))))

  :on-resize
  (fn [screen entities]
    (height! screen 600))
  )

(defscreen debug-screen
  :on-show
  (fn [screen entities]
    (update! screen :camera (orthographic) :renderer (stage))
    (assoc (label "0" (color :white))
            :id :fps
            :x 5))

  :on-render
  (fn [screen entities]
    (->> (for [entity entities]
           (case (:id entity)
             :fps (doto entity (label! :set-text (str "FPS:" (game :fps) " W:" (game :width) " H:" (game :height))))
             entity))
         (render! screen)))

  :on-resize
  (fn [screen entities]
    (height! screen 600))
  )

(defscreen main-screen
  :on-show
  (fn [screen entities]
    (update! screen :renderer (stage) :camera (orthographic))
    (assoc (label "Hello world!" (color :white)) :x 5 :y 40))

  :on-render
  (fn [screen entities]
    (clear!)
    (render! screen entities))

  :on-key-down
  (fn [screen entities]
    (cond
      (= (:key screen) (key-code :f5)) (on-gl (set-screen! caves2-game title-screen debug-screen))))

  :on-resize
  (fn [screen entities]
    (height! screen 600))
  )

(defscreen blank-screen
  :on-render
  (fn [screen entities]
    (clear!))

  :on-key-down
  (fn [screen entities]
    (cond
      (= (:key screen) (key-code :f5)) (on-gl (set-screen! caves2-game title-screen debug-screen))))
  )

(set-screen-wrapper! (fn [screen screen-fn]
                       (try (screen-fn)
                         (catch Exception e
                           (.printStackTrace e)
                           (set-screen! caves2-game blank-screen)))))

(defgame caves2-game
  :on-create
  (fn [this]
    (set-screen! this title-screen debug-screen)))


(-> title-screen :entities deref)

(-> main-screen :entities deref)
