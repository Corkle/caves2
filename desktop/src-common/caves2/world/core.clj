(ns caves2.world.core)

(defrecord World [tiles])
(defrecord Tile [kind img size])

(def tiles
  {:floor (->Tile :floor "stone-black-64.jpg" [1 1])
   :wall (->Tile :wall "stone-wall-64.jpg" [1 1])})



(defn- get-tile-from-tiles [tiles [x y]]
  (get-in tiles [y x]))

(defn- random-coordinate [world]
  (let [tiles (:tiles world)
        rows (count tiles)
        cols (count (first tiles))]
    [(rand-int cols) (rand-int rows)]))

(defn- get-tile [world coord]
  (get-tile-from-tiles (:tiles world) coord))

(defn- get-tile-kind [world coord]
  (:kind (get-tile world coord)))

(defn- random-tiles [world-size]
  (let [[cols rows] world-size]
    (letfn [(random-tile []
                         (tiles (rand-nth [:floor :wall])))
            (random-row []
                        (vec (repeatedly cols random-tile)))]
           (vec (repeatedly rows random-row)))))

(defn- get-smoothed-tile [block]
  (let [tile-counts (frequencies (map :kind block))
        floor-threshold 5
        floor-count (get tile-counts :floor 0)
        result (if (>= floor-count floor-threshold)
                 :floor
                 :wall)]
    (tiles result)))

(defn- block-coords [x y]
  (for [dx [-1 0 1]
        dy [-1 0 1]]
    [(+ x dx) (+ y dy)]))

(defn- get-block [tiles x y]
  (map (partial get-tile-from-tiles tiles)
       (block-coords x y)))

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

(defn find-empty-tile [world]
  (loop [coord (random-coordinate world)]
    (if (#{:floor} (get-tile-kind world coord))
      coord
      (recur (random-coordinate world)))))

(defn random-world [world-size]
  (let [world (new World (random-tiles world-size))
        world (nth (iterate smooth-world world) 3)]
    world))
