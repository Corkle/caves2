(ns caves2.world.core
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]))

(defrecord World [tiles])
(defrecord Tile [kind img size])

(def tiles
  {:floor (new Tile :floor "stone-black.jpg" [1 1])
   :wall (new Tile :wall "stone-wall.jpg" [1 1])})

(defn- get-tile [tiles x y]
  (get-in tiles [y x]))

(defn- random-tiles [world-size]
  (let [[cols rows] world-size]
    (letfn [(random-tile []
                         (tiles (rand-nth [:floor :wall])))
            (random-row []
                        (vec (repeatedly cols random-tile)))]
           (vec (repeatedly rows random-row)))))

(defn random-world [world-size]
  (new World (random-tiles world-size)))

(random-world [3 3])
