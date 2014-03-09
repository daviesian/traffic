(defproject traffic "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [lein-cljsbuild "1.0.2"]
				 [org.clojure/clojurescript "0.0-2173"]
				 [compojure "1.1.6"]
				 [hiccup "1.0.5"]]
  :plugins [[lein-ring "0.8.7"]
            [lein-cljsbuild "1.0.2"]]
  :ring {:handler traffic.routes/app}
  :cljsbuild {
    :builds [{:source-paths ["src-cljs"]
              :compiler {:output-to "resources/public/js/main.js"
						 :output-dir "resources/public/js"
                         :optimizations :whitespace
                         :pretty-print true
						 ;;:source-map "resources/public/js/main.js.map"
						 }}]})
