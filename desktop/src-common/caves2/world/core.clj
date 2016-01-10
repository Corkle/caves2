(ns caves2.world.core)

(defrecord World [tiles])
(defrecord Tile [kind img size])

(def tiles
  {:floor (new Tile :floor "grass.jpg" [1 1])
   :wall (new Tile :wall "wood.jpg" [1 1])})

(defn- get-tile [tiles x y]
  (get-in tiles [y x]))

(defn- block-coords [x y]
  (for [dx [-1 0 1]
        dy [-1 0 1]]
    [(+ x dx) (+ y dy)]))

(defn- get-block [tiles x y]
  (map (fn [[x y]]
         (get-tile tiles x y))
       (block-coords x y)))

(defn- get-smoothed-tile [block]
  (let [tile-counts (frequencies (map :kind block))
        floor-threshold 5
        floor-count (get tile-counts :floor 0)
        result (if (>= floor-count floor-threshold)
                 :floor
                 :wall)]
    (tiles result)))

(defn- get-smoothed-row [tiles y]
  (mapv (fn [x]
          (get-smoothed-tile (get-block tiles x y)))
        (range (count (first tiles)))))

(defn- get-smoothed-tiles [tiles]
  (mapv (fn [y]
          (get-smoothed-row tiles y))
        (range (count tiles))))

(defn smooth-world [{:keys [tiles] :as world}]
  (assoc world :tiles (get-smoothed-tiles tiles)))

(defn- random-tiles [world-size]
  (let [[cols rows] world-size]
    (letfn [(random-tile []
                         (tiles (rand-nth [:floor :wall])))
            (random-row []
                        (vec (repeatedly cols random-tile)))]
           (vec (repeatedly rows random-row)))))

(defn random-world [world-size]
  (let [world (new World (random-tiles world-size))
        world (nth (iterate smooth-world world) 0)]
    world))
