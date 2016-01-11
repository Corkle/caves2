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

(defn- get-location-label [location scale x y]
  (assoc (label (str "Location:" location) (color :red)) :x (scale x) :y (scale y)))

(defn- get-viewport-tiles [tiles start-x end-x start-y end-y]
  (map #(subvec % start-x end-x) (subvec tiles start-y end-y)))

(defn- get-crosshair [screen vcols vrows]
  (let [crosshair-x (int (/ vcols 2))
        crosshair-y (int (/ vrows 2))
        scale (:scale screen)]
    (assoc (texture "red-x.png") :x (scale crosshair-x) :y (scale crosshair-y) :width (scale 1) :height (scale 1))))

(defn- get-world-textures [vrows vcols start-x start-y end-x end-y tiles scale]
  (map (fn [row row-idx]
             (map (fn [{:keys [img size]} col-idx]
                    (assoc (texture img) :x (scale col-idx) :y (scale row-idx) :width (scale (first size)) :height (scale (second size)))
                  )
                  row
                  (iterate inc 0)))
           tiles
           (iterate inc 1)))

(defn get-tile-entities [screen]
  (let [world (:world screen)
        location (:location screen)
        tiles (:tiles world)
        scale (:scale screen)
        [vcols vrows] (:view-size screen)
        [start-x start-y end-x end-y] (get-viewport-coords location tiles vcols vrows)
        view-tiles (get-viewport-tiles tiles start-x end-x start-y end-y)
        ]
      (flatten [(get-world-textures vrows vcols start-x start-y end-x end-y view-tiles scale) (get-location-label location scale 0 1) (get-crosshair screen vcols vrows)])))

(defn refresh-tiles [screen entities]
  (let [new-tiles (get-tile-entities screen)
        tile-count (count new-tiles)]
    [new-tiles (drop tile-count entities)]))

(defn move [[x y] [dx dy]]
  [(+ x dx) (+ y dy)])
