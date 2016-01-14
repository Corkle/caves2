(ns caves2.core
  (:require [play-clj.core :refer :all]
            [play-clj.ui :refer :all]
            [play-clj.g2d :refer :all]
            [caves2.world.core :refer [random-world smooth-world]]
            [caves2.ui.entities.core :refer [draw-tiles draw-player move]]
            [caves2.entities.player :refer [make-player move-player]]))

(declare caves2-game title-screen main-screen tiles-screen debug-screen win-screen lose-screen)
(defrecord Game [scale view-size world])

(def scale-up (partial * 64))
(def view-size [15 10])
(def screen-size (vec (map scale-up view-size)))
(def world-size [100 60])

(defn new-game []
  (->Game scale-up view-size nil))

(defn reset-game [game]
  (let [new-world (random-world world-size)]
    (-> game
        (assoc :world new-world)
        (assoc-in [:world :player] (make-player new-world)))))

(def game-state (atom (new-game)))

;; ====================================================================================
;; Title Screen
;; ====================================================================================
(defscreen title-screen
  :on-show
  (fn [screen entities]
    (update! screen :renderer (stage) :camera (orthographic))
    (let [text1 (assoc (label "Welcome to the Caves of Clojure!" (color :white)) :x 300 :y 400)
          text2 (assoc (label "Press enter to play." (color :white)) :x 345 :y 350)
          block1 (assoc (texture "stone-black.jpg") :width (scale-up 2) :height (scale-up 2) :x (scale-up 0) :y (scale-up 1))
          block2 (assoc (texture "stone-wall.jpg") :width (scale-up 1) :height (scale-up 1) :x (scale-up 2) :y (scale-up 1))]
          [text1 text2 block1 block2]))

  :on-render
  (fn [screen entities]
    (clear!)
    (render! screen entities))

  :on-key-down
  (fn [screen entities]
    (cond
      (= (:key screen) (key-code :enter)) (do
                                            (reset! game-state (assoc (reset-game @game-state) :location [0 0]))
                                            (on-gl (set-screen! caves2-game tiles-screen main-screen debug-screen)))
      (= (:key screen) (key-code :f5)) (on-gl (set-screen! caves2-game title-screen debug-screen))))

  :on-resize
  (fn [screen entities]
    (height! screen (second screen-size)))
  )

;; ====================================================================================
;; Main Screen
;; ====================================================================================

(defscreen main-screen
  :on-show
  (fn [screen entities]
    (update! screen :renderer (stage) :camera (orthographic))
    (let [player (draw-player @game-state)
          text1 (assoc (label "Item Bar" (color :white)) :x 5 :y (scale-up (/ 1 2)))]
        [player text1]
      ))

  :on-render
  (fn [screen entities]
;;     (clear!)
    (render! screen entities))

  :on-key-down
  (fn [screen entities]
    (cond
      (= (:key screen) (key-code :enter)) (set-screen! caves2-game win-screen debug-screen)
      (= (:key screen) (key-code :r)) (screen! tiles-screen :on-tiles-changed)
      (= (:key screen) (key-code :t)) (do
                                        (swap! game-state update-in [:world] smooth-world)
                                        (screen! tiles-screen :on-tiles-changed))
      (= (:key screen) (key-code :w)) (do
                                        (swap! game-state update-in [:world] move-player :n)
                                        (screen! tiles-screen :on-tiles-changed)
                                        [(draw-player @game-state) (rest entities)])
      (= (:key screen) (key-code :a)) (do
                                        (swap! game-state update-in [:world] move-player :w)
                                        (screen! tiles-screen :on-tiles-changed)
                                        [(draw-player @game-state) (rest entities)])
      (= (:key screen) (key-code :s)) (do
                                        (swap! game-state update-in [:world] move-player :s)
                                        (screen! tiles-screen :on-tiles-changed)
                                        [(draw-player @game-state) (rest entities)])
      (= (:key screen) (key-code :d)) (do
                                        (swap! game-state update-in [:world] move-player :e)
                                        (screen! tiles-screen :on-tiles-changed)
                                        [(draw-player @game-state) (rest entities)])
      (= (:key screen) (key-code :f12)) entities
      :else (set-screen! caves2-game lose-screen debug-screen)))

  :on-resize
  (fn [screen entities]
    (height! screen (second screen-size)))
  )

;; ====================================================================================
;; Tiles Screen
;; ====================================================================================

(defscreen tiles-screen
  :on-show
  (fn [screen entities]
    (update! screen :renderer (stage) :camera (orthographic))
    [(draw-tiles @game-state)])

  :on-render
  (fn [screen entities]
    (clear!)
    (render! screen entities))

  :on-tiles-changed
  (fn [screen entities]
    (draw-tiles @game-state))

  :on-resize
  (fn [screen entities]
    (height! screen (second screen-size)))
  )

;; ====================================================================================
;; Win Screen
;; ====================================================================================
(defscreen win-screen
  :on-show
  (fn [screen entities]
    (update! screen :camera (orthographic) :renderer (stage))
    (let [text1 (assoc (label "Congratulations, you win!" (color :green)) :x 300 :y 400)
          text2 (assoc (label "Press escape to exit, anything else to restart." (color :white)) :x 240 :y 350)]
      [text1 text2]))

  :on-render
  (fn [screen entities]
    (clear!)
    (render! screen entities))

  :on-key-down
  (fn [screen entities]
    (cond
      (= (:key screen) (key-code :escape)) (app! :exit)
      :else (on-gl (set-screen! caves2-game title-screen debug-screen))))

  :on-resize
  (fn [screen entities]
    (height! screen (second screen-size)))
  )

;; ====================================================================================
;; Lose Screen
;; ====================================================================================
(defscreen lose-screen
  :on-show
  (fn [screen entities]
    (update! screen :camera (orthographic) :renderer (stage))
    (let [text1 (assoc (label "Sorry, better luck next time." (color :red)) :x 300 :y 400)
          text2 (assoc (label "Press escape to exit, anything else to restart." (color :white)) :x 240 :y 350)]
      [text1 text2]))

  :on-render
  (fn [screen entities]
    (clear!)
    (render! screen entities))

  :on-key-down
  (fn [screen entities]
    (cond
      (= (:key screen) (key-code :escape)) (app! :exit)
      :else (on-gl (set-screen! caves2-game title-screen debug-screen))))

  :on-resize
  (fn [screen entities]
    (height! screen (second screen-size)))
  )

;; ====================================================================================
;; Debug Screen
;; ====================================================================================
(defscreen debug-screen
  :on-show
  (fn [screen entities]
    (update! screen :camera (orthographic) :renderer (stage))
    (let [debugger (assoc (label "0" (color :green)) :id :debug :x 5 :show-debug true)]
      [debugger]))

  :on-render
  (fn [screen entities]
    (let [{{{:keys [location]} :player} :world} @game-state
          debugger (first entities)]
      (if (:show-debug debugger)
        (do
          (label! debugger :set-text (str "FPS:" (game :fps) " W:" (game :width) " H:" (game :height) " View:" view-size " Loc:" location))
          (render! screen [debugger (rest entities)])))))

  :on-resize
  (fn [screen entities]
    (height! screen (second screen-size)))

  :on-key-down
  (fn [screen entities]
    (let [debugger (first entities)]
      (cond
        (= (:key screen) (key-code :f12)) [(update debugger :show-debug not) (rest entities)]
        (= (:key screen) (key-code :f5)) (on-gl (set-screen! caves2-game title-screen debug-screen)))))
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
(swap! game-state assoc :game caves2-game)




(deref game-state)

(-> title-screen :entities deref)

(-> main-screen :entities deref)
(-> main-screen :screen deref)



(-> debug-screen :entities deref)

;; (-> win-screen :entities deref)

