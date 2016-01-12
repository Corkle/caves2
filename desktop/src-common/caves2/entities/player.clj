(ns caves2.entities.player
  (:use [caves2.entities.core :only [Entity]]
        [caves2.world.core :only [find-empty-tile]]))

(defrecord Player [id img location size])

(extend-type Player Entity
  (tick [this game]
        game))

(defn make-player [world]
  (->Player :player "cat.png" (find-empty-tile world) [1 1]))
