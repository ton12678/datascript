(ns datascript.test.serialization
  (:require
    [#?(:cljs cljs.reader :clj clojure.edn) :as edn]
    #?(:cljs [cljs.test    :as t :refer-macros [is are deftest testing]]
       :clj  [clojure.test :as t :refer        [is are deftest testing]])
    [datascript.core :as d]
    [datascript.db :as db]
    [datascript.test.core :as tdc])
    #?(:clj
      (:import [clojure.lang ExceptionInfo])))

(def readers
  { #?@(:cljs ["cljs.reader/read-string"  cljs.reader/read-string]
        :clj  ["clojure.edn/read-string"  #(clojure.edn/read-string {:readers d/data-readers} %)
               "clojure.core/read-string" read-string]) })

(deftest test-pr-read
  (doseq [[r read-fn] readers]
    (testing r
      (let [d (db/datom 1 :name "Oleg" 17 true)]
        (is (= (pr-str d) "#datascript/Datom [1 :name \"Oleg\" 17 true]"))
        (is (= d (read-fn (pr-str d)))))
      
      (let [d (db/datom 1 :name 3)]
        (is (= (pr-str d) "#datascript/Datom [1 :name 3 536870912 true]"))
        (is (= d (read-fn (pr-str d)))))
      
      (let [db (-> (d/empty-db {:name {:db/unique :db.unique/identity}})
                   (d/db-with [ [:db/add 1 :name "Petr"]
                                [:db/add 1 :age 44] ])
                   (d/db-with [ [:db/add 2 :name "Ivan"] ]))]
        (is (= (pr-str db)
               (str "#datascript/DB {"
                    ":schema {:name {:db/unique :db.unique/identity}}, "
                    ":datoms ["
                      "[1 :age 44 536870913] "
                      "[1 :name \"Petr\" 536870913] "
                      "[2 :name \"Ivan\" 536870914]"
                    "]}")))
        (is (= db (read-fn (pr-str db))))))))

#?(:clj
  (deftest test-reader-literals
    (is (= #datascript/Datom [1 :name "Oleg"]
                    (db/datom 1 :name "Oleg")))
    (is (= #datascript/Datom [1 :name "Oleg" 100 false]
                    (db/datom 1 :name "Oleg" 100 false)))
    ;; not supported because IRecord print method is hard-coded into Compiler
    #_(is (= #datascript/DB {:schema {:name {:db/unique :db.unique/identity}}
                           :datoms [[1 :name "Oleg" 100] [1 :age 14 100] [2 :name "Petr" 101]]}
           (d/init-db 
             [ (db/datom 1 :name "Oleg" 100)
               (db/datom 1 :age 14 100)
               (db/datom 2 :name "Petr" 101) ]
             {:name {:db/unique :db.unique/identity}})))))


(def data
  [[1 :name    "Petr"]
   [1 :aka     "Devil"]
   [1 :aka     "Tupen"]
   [1 :age     15]
   [1 :follows 2]
   [1 :email   "petr@gmail.com"]
   [1 :avatar  10]
   [10 :url    "http://"]
   [1 :attach  { :some-key :some-value }]
   [2 :name    "Oleg"]
   [2 :age     30]
   [2 :email   "oleg@gmail.com"]
   [2 :attach  [ :just :values ]]
   [3 :name    "Ivan"]
   [3 :age     15]
   [3 :follows 2]
   [3 :attach  { :another :map }]
   [3 :avatar  30]
   [4 :name    "Nick" (+ d/tx0 100)]
   [(+ d/tx0 100) :txInstant 0xdeadbeef]
   [30 :url    "https://" ]])


(def schema 
  { :name    { } ;; nothing special about name
    :aka     { :db/cardinality :db.cardinality/many }
    :age     { :db/index true }
    :follows { :db/valueType :db.type/ref }
    :email   { :db/unique :db.unique/identity }
    :avatar  { :db/valueType :db.type/ref, :db/isComponent true }
    :url     { } ;; just a component prop
    :attach  { } ;; should skip index
})


(deftest test-init-db
  (let [db-init     (-> (map #(apply d/datom %) data)
                        (d/init-db schema))
        db-transact (->> (map (fn [[e a v]] [:db/add e a v]) data)
                         (d/db-with (d/empty-db schema)))]
    (testing "db-init produces the same result as regular transactions"
      (is (= db-init db-transact)))

    (testing "db-init produces the same max-eid as regular transactions"
      (let [assertions [ [:db/add -1 :name "Lex"] ]]
        (is (= (d/db-with db-init assertions)
               (d/db-with db-transact assertions)))))
    
    (testing "Roundtrip"
      (doseq [[r read-fn] readers]
        (testing r
          (is (= db-init (read-fn (pr-str db-init)))))))))
