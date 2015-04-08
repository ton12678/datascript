(ns datascript.test.validation
  (:require-macros
    [cemerick.cljs.test :refer [is are deftest testing]])
  (:require
    [datascript.core :as dc]
    [datascript :as d]
    [cemerick.cljs.test :as t]
    [datascript.test.core :as tdc]))

(deftest test-with-validation
  (let [db (d/empty-db {:profile { :db/valueType :db.type/ref }})]
    (are [tx] (thrown-with-msg? js/Error #"Expected number or lookup ref for entity id" (d/db-with db tx))
      [[:db/add nil :name "Ivan"]]
      [[:db/add "aaa" :name "Ivan"]]
      [{:db/id "aaa" :name "Ivan"}])
    
    (are [tx] (thrown-with-msg? js/Error #"Bad entity attribute" (d/db-with db tx))
      [[:db/add -1 nil "Ivan"]]
      [[:db/add -1 17 "Ivan"]]
      [{:db/id -1 17 "Ivan"}])
    
    (are [tx] (thrown-with-msg? js/Error #"Cannot store nil as a value" (d/db-with db tx))
      [[:db/add -1 :name nil]]
      [{:db/id -1 :name nil}])
    
    (are [tx] (thrown-with-msg? js/Error #"Expected number or lookup ref for entity id" (d/db-with db tx))
      [[:db/add -1 :profile "aaa"]]
      [{:db/id -1 :profile "aaa"}])
    
    (is (thrown-with-msg? js/Error #"Unknown operation" (d/db-with db [["aaa" :name "Ivan"]])))
    (is (thrown-with-msg? js/Error #"Bad entity type at" (d/db-with db [:db/add "aaa" :name "Ivan"])))
    (is (thrown-with-msg? js/Error #"Bad transaction data" (d/db-with db {:profile "aaa"})))))

(deftest test-unique
  (let [db (d/db-with (d/empty-db {:name { :db/unique :db.unique/value }})
                      [[:db/add 1 :name "Ivan"]
                       [:db/add 2 :name "Petr"]])]
    (are [tx] (thrown-with-msg? js/Error #"unique constraint" (d/db-with db tx))
      [[:db/add 3 :name "Ivan"]]
      [{:db/add 3 :name "Petr"}])
    (d/db-with db [[:db/add 3 :name "Igor"]])
    (d/db-with db [[:db/add 3 :nick "Ivan"]])))
