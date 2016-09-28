<img src="https://dl.dropboxusercontent.com/u/561580/imgs/datascript_logo.svg">

> What if creating a database would be as cheap as creating a Hashmap?

An immutable in-memory database and Datalog query engine in Clojure and ClojureScript.

DataScript is meant to run inside the browser. It is cheap to create, quick to query and ephemeral. You create a database on page load, put some data in it, track changes, do queries and forget about it when the user closes the page.

DataScript databases are immutable and based on persistent data structures. In fact, they’re more like data structures than databases (think Hashmap). Unlike querying a real SQL DB, when you query DataScript, it all comes down to a Hashmap lookup. Or series of lookups. Or array iteration. There’s no particular overhead to it. You put a little data in it, it’s fast. You put in a lot of data, well, at least it has indexes. That should do better than you filtering an array by hand anyway. The thing is really lightweight.

The intention with DataScript is to be a basic building block in client-side applications that needs to track a lot of state during their lifetime. There’s a lot of benefits:

- Central, uniform approach to manage all application state. Clients working with state become decoupled and independent: rendering, server sync, undo/redo do not interfere with each other.
- Immutability simplifies things even in a single-threaded browser environment. Keep track of app state evolution, rewind to any point in time, always render consistent state, sync in background without locking anybody.
- Datalog query engine to answer non-trivial questions about current app state.
- Structured format to track data coming in and out of DB. Datalog queries can be run against it too.

## Resources <a href="https://gitter.im/tonsky/datascript?utm_source=badge&amp;utm_medium=badge&amp;utm_campaign=pr-badge&amp;utm_content=badge"><img src="https://camo.githubusercontent.com/da2edb525cde1455a622c58c0effc3a90b9a181c/68747470733a2f2f6261646765732e6769747465722e696d2f4a6f696e253230436861742e737667" alt="Gitter" data-canonical-src="https://badges.gitter.im/Join%20Chat.svg" style="max-width:100%;"></a>

Books:

- [Learning ClojureScript](https://www.packtpub.com/web-development/learning-clojurescript) has a chapter on DataScript

Docs:

- [Getting started](https://github.com/tonsky/datascript/wiki/Getting-started)
- [Tutorials](https://github.com/kristianmandrup/datascript-tutorial)
- [DataScript 101](http://udayv.com/clojurescript/clojure/2016/04/28/datascript101/)
- [Tips & tricks](https://github.com/tonsky/datascript/wiki/Tips-&-tricks)

Posts:

- [How DataScript fits into the current webdev ecosystem](http://tonsky.me/blog/decomposing-web-app-development/)
- [DataScript internals explained](http://tonsky.me/blog/datascript-internals/)
- [Sketch of client/server reactive architecture](http://tonsky.me/blog/the-web-after-tomorrow/)

Talks:

- “Frontend with Joy” talk (FPConf, August 2015): [video](https://www.youtube.com/watch?v=cRWrrHPrk9g) in Russian
- “Programming Web UI with Database in a Browser” talk (PolyConf, July 2015): [slides](https://dl.dropboxusercontent.com/u/561580/conferences/2015.07%20polyconf.pdf), [video](https://www.youtube.com/watch?v=1dr-CzMMDD8)
- “DataScript for Web Development” talk (Clojure eXchange, Dec 2014): [slides](https://dl.dropboxusercontent.com/u/561580/conferences/2014.12%20clojure%20eXchange.pdf), [video](https://skillsmatter.com/skillscasts/6038-datascript-for-web-development)
- “Building ToDo list with DataScript” webinar (ClojureScript NYC, Dec 2014): [video](http://vimeo.com/114688970), [app](https://github.com/tonsky/datascript-todo)
- DataScript hangout (May 2014, in Russian): [video](http://www.youtube.com/watch?v=jhBC81pczZY)

Projects using DataScript:

- [Precursor](http://precursorapp.com/), collaborative prototyping tool
- [LightMesh](http://lightmesh.com/), datacenter management
- [Cognician](https://www.cognician.com/), coaching platform
- [bitfountain](http://www.bitfountain.io/), iOS courses
- [PartsBox](https://partsbox.io/), electronic parts management
- [I am Fy](https://www.iamfy.co/), accessories e-shop
- [Acha-acha](http://acha-acha.co/), github achievements ([sources](https://github.com/someteam/acha), [code walkthrough](http://tonsky.me/blog/acha-acha/))
- [Radiant](http://www.stuttaford.me/radiant/), datalog sketchpad ([sources](https://github.com/robert-stuttaford/stuttaford.me/tree/master/src/stuttaford/radiant))
- [Showkr](http://showkr.solovyov.net), flickr gallery viewer ([sources](https://github.com/piranha/showkr))
- [Zetawar](http://www.zetawar.com), turn-based tactical strategy game

Related projects:

- [DataScript-Transit](https://github.com/tonsky/datascript-transit), transit serialization for database and datoms
- [Posh](https://github.com/mpdairy/posh), lib that lets you use a single DataScript db to store Reagent app state
- [DataScript-mori](https://github.com/typeetfunc/datascript-mori), DataScript & Mori wrapper for use from JS
- [DatSync](https://github.com/metasoarous/datsync), Datomic ↔︎ DataScript syncing/replication utilities
- [Intension](https://github.com/alandipert/intension), lib to convert associative structures to in-memory databases for querying them
- [Datamaps](https://github.com/djjolicoeur/datamaps),  lib designed to leverage datalog queries to query arbitrary maps.

Demo applications:

- [Localisation Demo with Om Next](http://simonb.com/blog/2016/01/24/om-next-datascript-localisation-demo/)
- ToDo, task manager demo app (persistence via localStorage and transit, filtering, undo/redo): [sources](https://github.com/tonsky/datascript-todo), [live](http://tonsky.me/datascript-todo/)
- CatChat, chat demo app: [sources](https://github.com/tonsky/datascript-chat), [code walkthrough](http://tonsky.me/blog/datascript-chat/), [live](http://tonsky.me/datascript-chat/)
- clj-crud, demo CRUD app: [sources](https://github.com/thegeez/clj-crud), [blog post](http://thegeez.net/2014/04/30/datascript_clojure_web_app.html)
- [OmNext TodoMVC](https://github.com/madvas/todomvc-omnext-datomic-datascript)

## Usage examples [![Build Status](https://travis-ci.org/tonsky/datascript.svg?branch=master)](https://travis-ci.org/tonsky/datascript)

For more examples, see [our acceptance test suite](test/datascript/test/).

```clj
:dependencies [
  [org.clojure/clojure "1.7.0"]
  [org.clojure/clojurescript "1.7.228"]
  [datascript "0.15.4"]
]
```

_Note:_ Since DataScript 0.13 **main namespace to include is `datascript.core`**, not `datascript`. This was done because usage of top-level namespaces is discouraged and even generates a warning in CLJS. Old `datascript.core`, if you were using it, was renamed to `datascript.db`

_WARN:_ *ClojureScript versions 1.7.48, 1.7.58 and 1.7.107 [break DataScript](https://github.com/tonsky/datascript/issues/108). Use either 1.7.28, 1.7.122 or newer*

_Note:_ DataScript 0.11.3 and later requires CLJS 0.0-3291 or later

_Note:_ DataScript 0.11.2 and later requires Clojure 1.7.0-beta2 or later

_Note:_ DataScript 0.11.0 and later will only work with CLJS 0.0-3196 and later

```clj
(require '[datascript.core :as d])

;; Implicit join, multi-valued attribute

(let [schema {:aka {:db/cardinality :db.cardinality/many}}
      conn   (d/create-conn schema)]
  (d/transact! conn [ { :db/id -1
                        :name  "Maksim"
                        :age   45
                        :aka   ["Maks Otto von Stirlitz", "Jack Ryan"] } ])
  (d/q '[ :find  ?n ?a
          :where [?e :aka "Maks Otto von Stirlitz"]
                 [?e :name ?n]
                 [?e :age  ?a] ]
       @conn))

;; => #{ ["Maksim" 45] }


;; Destructuring, function call, predicate call, query over collection

(d/q '[ :find  ?k ?x
        :in    [[?k [?min ?max]] ...] ?range
        :where [(?range ?min ?max) [?x ...]]
               [(even? ?x)] ]
      { :a [1 7], :b [2 4] }
      range)

;; => #{ [:a 2] [:a 4] [:a 6] [:b 2] }


;; Recursive rule

(d/q '[ :find  ?u1 ?u2
        :in    $ %
        :where (follows ?u1 ?u2) ]
      [ [1 :follows 2]
        [2 :follows 3]
        [3 :follows 4] ]
     '[ [(follows ?e1 ?e2)
         [?e1 :follows ?e2]]
        [(follows ?e1 ?e2)
         [?e1 :follows ?t]
         (follows ?t ?e2)] ])

;; => #{ [1 2] [1 3] [1 4]
;;       [2 3] [2 4]
;;       [3 4] }


;; Aggregates

(d/q '[ :find ?color (max ?amount ?x) (min ?amount ?x)
        :in   [[?color ?x]] ?amount ]
     [[:red 10]  [:red 20] [:red 30] [:red 40] [:red 50]
      [:blue 7] [:blue 8]]
     3)

;; => [[:red  [30 40 50] [10 20 30]]
;;     [:blue [7 8] [7 8]]]
```

## Using from vanilla JS

DataScript can be used from any JS engine without additional dependencies:

```html
<script src="https://github.com/tonsky/datascript/releases/download/0.15.4/datascript-0.15.4.min.js"></script>
```

or as a CommonJS module ([npm page](https://www.npmjs.org/package/datascript)):

```
npm install datascript
```

```js
var ds = require('datascript');
```

or as a RequireJS module:

```js
require(['datascript'], function(ds) { ... });
```

Queries:

* Query and rules should be EDN passed as strings
* Results of `q` are returned as regular JS arrays

Entities:

* Entities returned by `entity` call are lazy as in Clojure
* Use `e.get("prop")`, `e.get(":db/id")`, `e.db` to access entity properties
* Entities implement ECMAScript 6 Map interface (has/get/keys/...)

Transactions:

* Use strings such as `":db/id"`, `":db/add"`, etc. instead of db-namespaced keywords
* Use regular JS arrays and objects to pass data to `transact` and `db_with`

Transaction reports:

* `report.tempids` has string keys (`"-1"` for entity tempid `-1`), use `resolve_tempid` to set up a correspondence

Check out [test/js/tests.js](test/js/tests.js) for usage examples.

## Project status

Beta quality. Most of the features done, expecting some API polishing (breaking) and performance optimizations. No docs at the moment, use examples & Datomic documentation.

The following features are supported:

* Database as a value: each DB is an immutable value. New DBs are created on top of old ones, but old ones stay perfectly valid too
* Triple store model
* EAVT, AEVT and AVET indexes
* Multi-valued attributes via `:db/cardinality :db.cardinality/many`
* Lazy entities and `:db/valueType :db.type/ref` auto-expansion
* Database “mutations” via `transact!`
* Callback-based analogue to txReportQueue via `listen!`
* Direct index lookup and iteration via `datoms` and `seek-datoms`
* Filtered databases via `filter`
* Lookup refs
* Unique constraints, upsert
* Pull API (thx [David Thomas Hume](https://github.com/dthume))

Query engine features:

* Implicit joins
* Query over DB or regular collections
* Parameterized queries via `:in` clause
* Tuple, collection, relation binding forms in `:in` clause
* Query over multiple DB/collections
* Predicates and user functions in query
* Rules, recursive rules
* Aggregates
* Find specifications

Interface differences:

* Conn is just an atom storing last DB value, use `@conn` instead of `(d/db conn)`
* Instead of `#db/id[:db.part/user -100]` just use `-100` in place of `:db/id` or entity id
* Transactor functions can be called as `[:db.fn/call f args]` where `f` is a function reference and will take db as first argument (thx [@thegeez](https://github.com/thegeez))
* In ClojureScript, custom query functions and aggregates should be passed as source instead of being referenced by symbol (due to lack of `resolve` in CLJS)
* Custom aggregate functions are called via aggregate keyword: `:find (aggregate ?myfn ?e) :in $ ?myfn`
* Additional `:db.fn/retractAttribute` shortcut
* Transactions are not annotated by default with `:db/txInstant`

Expected soon:

* Better error reporting
* Proper documentation
* `not`, `not-join`, `or` and `or-join` datalog clauses

## Differences from Datomic

* DataScript is built totally from scratch and is not related by any means to the popular Clojure database Datomic
* Runs in a browser and/or in a JVM
* Simplified schema, not queryable
* Attributes do not have to be declared in advance. Put them to schema only when you need special behaviour from them
* Any type can be used for values
* No `:db/ident` attributes, keywords are _literally_ attribute values, no integer id behind them
* No schema migrations
* No cache segments management, no laziness. Entire DB must reside in memory
* No facilities to persist, transfer over the wire or sync DB with the server
* No pluggable storage options, no full-text search, no partitions
* No external dependencies
* Free

Aimed at interactive, long-living browser applications, DataScript DBs operate in constant space. If you do not add new entities, just update existing ones, or clean up database from time to time, memory consumption will be limited. This is unlike Datomic which keeps history of all changes, thus grows monotonically. DataScript does not track history by default, but you can do it via your own code if needed.

Some of the features are omitted intentionally. Different apps have different needs in storing/transfering/keeping track of DB state. DataScript is a foundation to build exactly the right storage solution for your needs without selling too much “vision”.

## License

Copyright © 2014–2015 Nikita Prokopov

Licensed under Eclipse Public License (see [LICENSE](LICENSE)).
