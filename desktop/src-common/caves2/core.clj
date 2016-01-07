(ns caves2.core
  (:require [play-clj.core :refer :all]
            [play-clj.ui :refer :all]))

(declare caves2-game title-screen main-screen debug-screen win-screen lose-screen-onshow-)

;; ====================================================================================
;; Title Screen
;; ====================================================================================
(defscreen title-screen
  :on-show
  (fn [screen entities]
    (update! screen :renderer (stage) :camera (orthographic))
    (let [text1 (assoc (label "Welcome to the Caves of Clojure!" (color :white)) :x 300 :y 400)
          text2 (assoc (label "Press enter to win, anything else to lose." (color :white)) :x 285 :y 350)]
          [text1 text2]))

  :on-render
  (fn [screen entities]
    (clear!)
    (render! screen entities))

  :on-key-down
  (fn [screen entities]
    (cond
      (= (:key screen) (key-code :enter)) (set-screen! caves2-game win-screen debug-screen)
      (= (:key screen) (key-code :space)) (set-screen! caves2-game main-screen debug-screen)
      (= (:key screen) (key-code :f5)) (on-gl (set-screen! caves2-game title-screen debug-screen))))

  :on-resize
  (fn [screen entities]
    (height! screen 600))
  )

;; ====================================================================================
;; Main Screen
;; ====================================================================================
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
      (= (:key screen) (key-code :r)) (on-gl (set-screen! caves2-game title-screen debug-screen))))

  :on-resize
  (fn [screen entities]
    (height! screen 600))
  )

;; ====================================================================================
;; Win Screen
;; ====================================================================================
(defscreen win-screen
  :on-show
  (fn [screen entities]
    (update! screen :camera (orthographic) :renderer (stage))
    (let [text1 (assoc (label "Congratulations, you win!" (color :green)) :x 300 :y 400)
          text2 (assoc (label "Press escape to exit, anything else to restart." (color :white)) :x 300 :y 400)]
      [text1 text2]))

  :on-render
  (fn [screen entities]
    (clear!)
    (render! screen entities))
  )

;; ====================================================================================
;; Lose Screen
;; ====================================================================================
(defscreen lose-screen
  :on-show
  (fn [screen entities]
    (update! screen :camera (orthographic) :renderer (stage))
    (assoc (label "Sorry, better luck next time." (color :red)))
    (assoc (label "Press escape to exit, anything else to restart." (color :white))))

  :on-render
  (fn [screen entities]
    (clear!)
    (render! screen entities))
  )

;; ====================================================================================
;; Debug Screen
;; ====================================================================================
(defscreen debug-screen
  :on-show
  (fn [screen entities]
    (update! screen :camera (orthographic) :renderer (stage))
    (let [debugger (assoc (label "0" (color :white)) :id :fps :x 5 :show-debugger false)][debugger]))

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

  :on-key-down
  (fn [screen entities]
    (cond
;;       (= (:key screen) (key-code :f12)) (update )
      (= (:key screen) (key-code :f5)) (on-gl (set-screen! caves2-game title-screen debug-screen))))
  )

;; ====================================================================================
;; Blank Screen
;; ====================================================================================
(defscreen blank-screen
  :on-render
  (fn [screen entities]
    (clear!))

  :on-key-down
  (fn [screen entities]
    (cond
      (= (:key screen) (key-code :f5)) (on-gl (set-screen! caves2-game title-screen debug-screen))))
  )

;; ====================================================================================
;; REPL error catch - go to blank screen
;; ====================================================================================
(set-screen-wrapper! (fn [screen screen-fn]
                       (try (screen-fn)
                         (catch Exception e
                           (.printStackTrace e)
                           (set-screen! caves2-game blank-screen)))))

;; ====================================================================================
;; Game
;; ====================================================================================
(defgame caves2-game
  :on-create
  (fn [this]
    (set-screen! this title-screen debug-screen)))




(-> title-screen :entities deref)

(-> main-screen :entities deref)

(-> debug-screen :entities deref)
