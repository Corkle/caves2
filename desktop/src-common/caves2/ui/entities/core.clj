(ns caves2.ui.entities.core
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]))

(defn get-tile-entities [screen]
  (let [world (:world screen)
        tiles (:tiles world)
        scale-up (:scale screen)
        screen-size (get screen :view-size)
        [cols rows] screen-size
        vcols cols
        vrows rows
        from-center {:x (int (Math/floor (/ vcols 2))) :y (int (Math/floor (/ vrows 2)))}
        start-x 0
        start-y (inc 0)
        end-x (+ start-x vcols)
        end-y (+ start-y (dec vrows))
        view-map (map #(subvec % start-x end-x) (subvec tiles start-y end-y))
        ]
      (flatten
      (map (fn [row row-idx]
           (map (fn [{:keys [img size]} col-idx]
                  (assoc (texture img) :x (scale-up col-idx) :y (scale-up row-idx) :width (scale-up (first size)) :height (scale-up (second size)))
                  )
                row
                (iterate inc start-x)))
         view-map
         (iterate inc start-y))))
  )
