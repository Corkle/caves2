(ns caves2.entities.player
  (:use [caves2.entities.core :only [Entity]]
        [caves2.entities.aspects.mobile :only [Mobile move can-move?]]
        [caves2.entities.aspects.digger :only [Digger dig can-dig?]]
        [caves2.coords :only [destination-coords]]
        [caves2.world.core :only [find-empty-tile get-tile-kind set-tile-floor]]))

(defrecord Player [id img location size])

(defn check-tile
  "Check that the tile at the destination passes the given predicate."
  [world dest pred]
  (pred (get-tile-kind world dest)))

(extend-type Player Entity
  (tick [this world]
        world))

(extend-type Player Mobile
  (move [this world dest]
        {:pre [(can-move? this world dest)]}
        (assoc-in world [:player :location] dest))
  (can-move? [this world dest]
             (check-tile world dest #{:floor})))

(extend-type Player Digger
  (dig [this world dest]
       {:pre [(can-dig? this world dest)]}
       (set-tile-floor world dest))
  (can-dig? [this world dest]
            (check-tile world dest #{:wall})))

(defn make-player [world]
  (->Player :player "cat.png" (find-empty-tile world) [1 1]))

(defn move-player [world dir]
  (let [player (:player world)
        target (destination-coords (:location player) dir)]
    (cond
      (can-move? player world target) (move player world target)
      (can-dig? player world target) (dig player world target)
      :else world)))
