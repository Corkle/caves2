(ns caves2.core.desktop-launcher
  (:require [caves2.core :refer :all])
  (:import [com.badlogic.gdx.backends.lwjgl LwjglApplication]
           [org.lwjgl.input Keyboard])
  (:gen-class))

(defn -main
  []
  (LwjglApplication. caves2-game "caves2" (first screen-size) (second screen-size))
  (Keyboard/enableRepeatEvents true))
