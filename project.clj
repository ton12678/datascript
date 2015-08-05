(defproject datascript "0.11.6"
  :description "An implementation of Datomic in-memory database and Datalog query engine in ClojureScript"
  :license {:name "Eclipse"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :url "https://github.com/tonsky/datascript"

  :dependencies [
    [org.clojure/clojure "1.7.0" :scope "provided"]
    [org.clojure/clojurescript "1.7.28" :scope "provided"]
  ]

  :global-vars {
    *warn-on-reflection* true
;;     *unchecked-math* :warn-on-boxed
  }

  :jvm-opts ["-Xmx2g" "-server"]

  :aliases {"test-clj"     ["run" "-m" "datascript.test/test-most"]
            "test-clj-all" ["run" "-m" "datascript.test/test-all"]
            "node-repl"    ["run" "-m" "user/node-repl"]
            "browser-repl" ["run" "-m" "user/browser-repl"]}

  :cljsbuild {
    :builds [
      { :id "release"
        :source-paths ["src" "bench/src"]
        :assert false
        :compiler {
          :output-to     "release-js/datascript.bare.js"
          :optimizations :advanced
          :pretty-print  false
          :elide-asserts true
          :output-wrapper false
          :warnings      {:single-segment-namespace false}
        }
        :notify-command ["release-js/wrap_bare.sh"]}
  ]}

  :profiles {
    :dev {
      :source-paths ["bench/src" "test" "dev"]
      :plugins [
        [lein-cljsbuild "1.0.6"]
      ]
      :cljsbuild {
        :builds [
          { :id "advanced"
            :source-paths ["src" "bench/src" "test"]
            :compiler {
              :main          datascript.test
              :output-to     "target/datascript.js"
;;               :output-dir    "target/advanced"
              :optimizations :advanced
              :source-map    "target/datascript.js.map"
              :pretty-print  true
              :warnings     {:single-segment-namespace false}
              :recompile-dependents false
            }}
          { :id "none"
            :source-paths ["src" "bench/src" "test" "dev"]
            :compiler {
              :main          datascript.test
              :output-to     "target/datascript.js"
              :output-dir    "target/none"
              :optimizations :none
              :source-map    true
              :warnings     {:single-segment-namespace false}
              :recompile-dependents false
            }}
        ]
      }
    }
  }

  :clean-targets ^{:protect false} [
    "target"
    "release-js/datascript.bare.js"
    "release-js/datascript.js"
  ]
)
