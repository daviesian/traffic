(ns traffic.cljs.core
  (:require [clojure.browser.repl :as repl]))

(def canvas (.getElementById js/document "canvas"))

(enable-console-print!)
(defn debugger []
  (js/eval "debugger"))


(repl/connect "http://localhost:9000/repl")


(def max-acc 1)
(def max-dec 5)
(def min-gap 2) ;; metres
(def ideal-gap-time 3) ;; seconds

(defn  get-mode [car ahead]
  (if (empty? ahead) :free :following))

(defn clamp [xmin xmax x]
  (max xmin (min x xmax)))

(defmulti get-acc get-mode)

(defmethod get-acc :free [car ahead]
  (let [;; Calculate target speed
        v-target (:v-max car)

        ;; Hence calculate acceleration
        acc      (- v-target (:v car))
        acc      (if (< 0 acc) (min acc max-acc) (max acc (- max-dec)))]
    acc))

(defmethod get-acc :following [car ahead]
  (let [;; Calculate distance to car ahead
        next-car (first ahead)
        gap      (- (:s next-car) (:s car))

        gap-time (/ gap (:v car))

        t-catch  (/ (- gap (* ideal-gap-time (:v next-car))) (* 0.5 (- (:v car) (:v next-car))))
        acc      (/ (- (:v next-car) (:v car)) t-catch)
        acc      (clamp (- max-dec) max-acc acc)
        ]
    (when (:log car)
      (js/console.clear)
      (println "GAP:" gap)
      (println "GAP TIME:" gap-time)
      (println "T-CATCH" t-catch)
      (println "ACC:" acc))
    acc))


(defn update-car [car ahead time]
  (let [delta-t  (- time (:last-t car))

        ;; Move car forward by v*dt
        car      (assoc car :s (+ (:s car) (* delta-t (:v car))))

        acc      (get-acc car ahead)
        ;; Update speed.
        car      (assoc car :v (+ (:v car) (* delta-t acc)))]
    (assoc car :last-t time :mode (get-mode car ahead))))

(defn ahead-of [cars car]
  (sort-by :s (filter #(< (:s car) (:s %)) cars)))

(defn redraw [cars]
  (let [ctx (.getContext canvas "2d")]
    (.clearRect ctx 0 0 1000 600)
    (doseq [c cars]
      (let [x (:s c)
            y 200]
        (.fillRect ctx x y 20 10)
        (.fillText ctx (int (:v c)) x y)
        ))))

(let [time (atom 0)
      cars (atom [{:s 10 :v 15 :v-max 20 :last-t 0 :log true}
                  {:s 50 :v 40 :v-max 18 :last-t 0 :log false}
                  {:s 250 :v 20 :v-max 10 :last-t 0}])]
  (js/setInterval (fn []
                    (swap! time #(+ 0.01 %))
                    (swap! cars (fn [cars] (map #(update-car % (ahead-of cars %) @time) cars)))
                    ;;(js/console.clear)
                    ;;(println "NEW STATE:\n" (interpose "\n" @cars))
                    (redraw @cars)) 5))
