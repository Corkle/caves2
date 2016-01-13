(ns caves2.ui.entities.core
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [play-clj.ui :refer :all]))

(defn- get-viewport-coords [location tiles vcols vrows]
  (let [[center-x center-y] location
        map-rows (count tiles)
        map-cols (count (first tiles))
        start-x (max 0 (- center-x (int (/ vcols 2))))
        start-y (max 0 (- center-y (int (/ vrows 2))))
        end-x (min (+ start-x vcols) map-cols)
        end-y (min (+ start-y (dec vrows)) map-rows)
        start-x (max 0 (- end-x vcols))
        start-y (max 0 (- end-y vrows))]
    [start-x start-y end-x end-y]))

(defn- get-viewport-tiles [tiles start-x end-x start-y end-y]
  (map #(subvec % start-x end-x) (subvec tiles start-y end-y)))

(defn- get-world-textures [vrows vcols start-x start-y end-x end-y tiles scale]
  (map (fn [row row-idx]
             (map (fn [{:keys [img size]} col-idx]
                    (assoc (texture img) :x (scale col-idx) :y (scale row-idx) :width (scale (first size)) :height (scale (second size)))
                  )
                  row
                  (iterate inc 0)))
           tiles
           (iterate inc 1)))

(defn- get-player [start-x start-y player scale]
  (let [[player-x player-y] (:location player)
        [w h] (:size player)
        x (- player-x start-x)
        y (- player-y start-y)]
    (assoc (texture (:img player)) :x (scale x) :y (scale y) :width (scale w) :height (scale h))))

(defn draw-tiles [game]
  (let [{:keys [world scale view-size entities]} game
        {{location :location } :player} entities
        tiles (:tiles world)
        [vcols vrows] view-size
        [start-x start-y end-x end-y] (get-viewport-coords location tiles vcols vrows)
        view-tiles (get-viewport-tiles tiles start-x end-x start-y end-y)
        tile-textures (get-world-textures vrows vcols start-x start-y end-x end-y view-tiles scale)]
    [tile-textures]
    ))

(defn draw-player [game]
  (let [{:keys [world scale view-size entities]} game
        {:keys [player]} entities
        player-pos (:location player)
        [player-x player-y] player-pos
        [vcols vrows] view-size
        [start-x start-y end-x end-y] (get-viewport-coords player-pos (:tiles world) vcols vrows)
        player-texture (get-player start-x start-y player scale)]
    [player-texture]
    ))


(defn move [[x y] [dx dy]]
  [(+ x dx) (+ y dy)])


