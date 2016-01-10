;; Anything you type in here will be executed
;; immediately with the results shown on the
;; right.


;;                [0 w w w f f f f f f f f f f f w w w w w w w w w f f f f f f]
;;                [1 w w w f f f f f f f f f f f w w w w w w w w w f f f f f f]
;;                [2 w w w f f f f f f f f f f f w w w w w w w w w f f f f f f]
;;                [3 w w V V V V V f f f f f f f w w w w w w w w w f f f f f f]
;;                [4 w w V V X V V f f f f f f f w w w w w w w w w f f f f f f]
;;                [5 w w V V V V V f f f f f f f w w w w w w w w w f f f f f f]
;;                [6 w w w f f f f f f f f f f f w w w w w w w w w f f f f f f]
;;                [7 w w w w w w w f f f f f f f w w w w w w w w w f f f f f f]

(def w {:img "stone-wall.jpg"})
(def f {:img "stone-black.jpg"})
(def v {:img "brick.jpg"})
(def X {:img "red.jpg"})


(def my-tiles [["0" 1 2 3 4 5 6 7 f f f f f f f w w w w w w w w w f f f f f f]
               ["1" w w w f f f f f f f f f f f w w w w w w w w w f f f f f f]
               ["2" w w w f f f f f f f f f f f w w w w w w w w w f f f f f f]
               ["3" w w v v v v v f f f f f f f w w w w w w w w w f f f f f f]
               ["4" w w v v X v v f f f f f f f w w w w w w w w w f f f f f f]
               ["5" w w v v v v v f f f f f f f w w w w w w w w w f f f f f f]
               ["6" w w w f f f f f f f f f f f w w w w w w w w w f f f f f f]
               ["7" w w w f f f f f f f f f f f w w w w w w w w w f f f f f f]])

(def player-pos {:x 5 :y 4})
(def view-size [5 3])
(def from-center {:x (int (Math/floor (/ (first view-size) 2))) :y (int (Math/floor (/ (second view-size) 2)))})
(def start-y (- (:y player-pos) (:y from-center)))
(def end-y (inc (+ (:y player-pos) (:y from-center))))
(def start-x (- (:x player-pos) (:x from-center)))
(def end-x (inc (+ (:x player-pos) (:x from-center))))


(def view-rows (subvec my-tiles start-y end-y))
(def view-map (map #(subvec % start-x end-x) view-rows))

(reduce (fn [ent-list row]
          (reduce (fn [ent-list tile]
                    (let [ent tile]
                      (conj ent-list ent)))
                  ent-list
                  row))
        []
        view-map)


(def my-screen {:world {:tiles my-tiles}})

(defn ent-coll [vis-map]
  (map (fn [row row-idx]
       (map (fn [tile col-idx]
              (hash-map :img tile :x col-idx :y row-idx))
            row
            (iterate inc start-x)))
     view-map
     (iterate inc start-y)))

(defn get-world-entities [screen]
  (let [world (:world screen)
        tiles (:tiles world)
        [cols rows] view-size
        vcols cols
        vrows rows
        start-x 0
        start-y 0
        end-x (+ start-x vcols)
        end-y (+ start-y vrows)]
    (flatten (for [[vrow-idx mrow-idx] (map vector (range 0 vrows) (range start-y end-y))
            :let [row-tiles (subvec (tiles mrow-idx) start-x end-x)]]
      (for [vcol-idx (range vcols)
            :let[{:keys [img size]} (row-tiles vcol-idx)]]
        (assoc (texture img) :x (scale-up vcol-idx) :y (scale-up vrow-idx) :width (scale-up (first size)) :height (scale-up (second size)))
        )))))

(defn get-world-entities [screen]
  (let [world (:world screen)
        tiles (:tiles world)
        [vcols vrows] view-size
        from-center {:x (int (Math/floor (/ vcols 2))) :y (int (Math/floor (/ vrows 2)))}
        start-x 3
        start-y 3
        end-x (+ start-x vcols)
        end-y (+ start-y vrows)
        view-map (map #(subvec % start-x end-x) (subvec tiles start-y end-y))]
    (flatten (map (fn [row row-idx]
           (map (fn [{:keys [img]} col-idx]
                  (assoc {} :img img :x col-idx :y row-idx))
                row
                (iterate inc start-x)))
         view-map
         (iterate inc start-y)
;;         (assoc (label "X" (color :yellow)) :x (scale-up vcol-idx) :y (scale-up vrow-idx))
;;         (assoc (texture img) :x (scale-up vcol-idx) :y (scale-up vrow-idx) :width (scale-up (first size)) :height (scale-up (second size)))


        ))))

(get-world-entities my-screen)


;; (defn tile-factory [img]
;;   (assoc (texture img) :x (scale-up (rand-int 20)) :y (scale-up (rand-int 15)) :width 40 :height 40))
