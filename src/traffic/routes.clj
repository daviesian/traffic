(ns traffic.routes
  (:use [compojure.core]
        [hiccup.core]
		[hiccup.page])
  (:require [compojure.route :as route]))

(def home
  (html5
   [:head
    [:title "Traffic"]]
   [:body
    [:canvas#canvas {:width 1000 :height 600 :style "width:1000px;height:600px;"}]
    (include-js "js/main.js")]))

(defroutes app
  (GET "/" [] home)
  (route/resources "/")
  (route/not-found "<h1>Page not found</h1>"))
