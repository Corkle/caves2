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

(def w "W")
(def f "F")
(def v "V")
(def X "X")


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
(def start-x 3)
(def end-x 8)

(def my-view (subvec my-tiles start-y end-y))

[(:x from-center) (:y from-center)]
(do start-y)

(map #(subvec % start-x end-x) my-view)




;; (defn tile-factory [img]
;;   (assoc (texture img) :x (scale-up (rand-int 20)) :y (scale-up (rand-int 15)) :width 40 :height 40))
