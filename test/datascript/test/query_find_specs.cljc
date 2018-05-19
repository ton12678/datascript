(ns datascript.test.query-find-specs
  (:require
    #?(:cljs [cljs.test    :as t :refer-macros [is are deftest testing]]
       :clj  [clojure.test :as t :refer        [is are deftest testing]])
    [datascript.core :as d]
    [datascript.db :as db]
    [datascript.test.core :as tdc]))

(def test-db (d/db-with
               (d/empty-db {:name {:db/order 0}
                            :age {:db/order 1}})
               [[:db/add 1 :name "Petr"]
                [:db/add 1 :age 44]
                [:db/add 2 :name "Ivan"]
                [:db/add 2 :age 25]
                [:db/add 3 :name "Sergey"]
                [:db/add 3 :age 11]]))

(deftest test-find-specs
  (is (= (set (d/q '[:find [?name ...]
                      :where [_ :name ?name]] test-db))
         #{"Ivan" "Petr" "Sergey"}))
  (is (= (d/q '[:find [?name ?age]
                :where [1 :name ?name]
                       [1 :age  ?age]] test-db)
         ["Petr" 44]))
  (is (= (d/q '[:find ?name .
                :where [1 :name ?name]] test-db)
         "Petr"))

  (testing "Multiple results get cut"
    (is (contains?
          #{["Petr" 44] ["Ivan" 25] ["Sergey" 11]}
          (d/q '[:find [?name ?age]
                 :where [?e :name ?name]
                        [?e :age  ?age]] test-db)))
    (is (contains?
          #{"Ivan" "Petr" "Sergey"}
          (d/q '[:find ?name .
                 :where [_ :name ?name]] test-db))))

  (testing "Aggregates work with find specs"
    (is (= (d/q '[:find [(count ?name) ...]
                  :where [_ :name ?name]] test-db)
           [3]))
    (is (= (d/q '[:find [(count ?name)]
                  :where [_ :name ?name]] test-db)
           [3]))
    (is (= (d/q '[:find (count ?name) .
                  :where [_ :name ?name]] test-db)
           3)))
)

#_(t/test-ns 'datascript.test.query-find-specs)
