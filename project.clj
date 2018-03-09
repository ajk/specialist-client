(defproject specialist-client "0.1.0-SNAPSHOT"
  :description "ClojureScript GraphQL client"
  :url "https://github.com/ajk/specialist-client"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.9.946" :scope "provided"]]

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-doo "0.1.8"]]

  :aliases {"test-cljs" ["with-profile" "test" "doo" "nashorn" "test" "once"]
            "test-all"  ["do" ["test"] ["test-cljs"]]}
  :profiles
  {:test {:cljsbuild
          {:builds
           {:test
            {:source-paths ["src" "test"]
             :compiler {:output-to "target/main.js"
                        :output-dir "target"
                        :language-in :ecmascript5
                        :main specialist-client.test-runner
                        :optimizations :simple}}}}}})
