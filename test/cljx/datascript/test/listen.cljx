(ns datascript.test.listen
  (:require-macros
    [cemerick.cljs.test :refer [is are deftest testing]])
  (:require
    [datascript.core :as dc]
    [datascript :as d]
    [cemerick.cljs.test :as t]
    [datascript.test.core :as tdc]))

(deftest test-listen!
  (let [conn    (d/create-conn)
        reports (atom [])]
    (d/transact! conn [[:db/add -1 :name "Alex"]
                       [:db/add -2 :name "Boris"]])
    (d/listen! conn :test #(swap! reports conj %))
    (d/transact! conn [[:db/add -1 :name "Dima"]
                       [:db/add -1 :age 19]
                       [:db/add -2 :name "Evgeny"]] {:some-metadata 1})
    (d/transact! conn [[:db/add -1 :name "Fedor"]
                       [:db/add 1 :name "Alex2"]         ;; should update
                       [:db/retract 2 :name "Not Boris"] ;; should be skipped
                       [:db/retract 4 :name "Evgeny"]])
    (d/unlisten! conn :test)
    (d/transact! conn [[:db/add -1 :name "Geogry"]])
    
    (is (= (:tx-data (first @reports))
           [(dc/Datom. 3 :name "Dima"   (+ d/tx0 2) true)
            (dc/Datom. 3 :age 19        (+ d/tx0 2) true)
            (dc/Datom. 4 :name "Evgeny" (+ d/tx0 2) true)]))
    (is (= (:tx-meta (first @reports))
           {:some-metadata 1}))
    (is (= (:tx-data (second @reports))
           [(dc/Datom. 5 :name "Fedor"  (+ d/tx0 3) true)
            (dc/Datom. 1 :name "Alex"   (+ d/tx0 3) false)  ;; update -> retract
            (dc/Datom. 1 :name "Alex2"  (+ d/tx0 3) true)   ;;         + add
            (dc/Datom. 4 :name "Evgeny" (+ d/tx0 3) false)]))
    (is (= (:tx-meta (second @reports))
           nil))
    ))
