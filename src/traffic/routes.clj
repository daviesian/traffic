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
	  (include-js "js/main.js")])) 

(defroutes app
  (GET "/" [] home)
  (route/resources "/")
  (route/not-found "<h1>Page not found</h1>"))

