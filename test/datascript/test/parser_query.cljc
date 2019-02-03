(ns datascript.test.parser-query
  (:require
    #?(:cljs [cljs.test    :as t :refer-macros [is are deftest testing]]
       :clj  [clojure.test :as t :refer        [is are deftest testing]])
    [datascript.core :as d]
    [datascript.db :as db]
    [datascript.parser :as dp]
    [datascript.test.core :as tdc])
    #?(:clj
      (:import [clojure.lang ExceptionInfo])))



(deftest validation
  (are [q msg] (thrown-msg? msg (dp/parse-query q))
    '[:find ?e :where [?x]]
    "Query for unknown vars: [?e]"

    '[:find ?e :with ?f :where [?e]]
    "Query for unknown vars: [?f]"
       
    '[:find ?e ?x ?t :in ?x :where [?e]]
    "Query for unknown vars: [?t]"
       
    '[:find ?x ?e :with ?y ?e :where [?x ?e ?y]]
    ":find and :with should not use same variables: [?e]"
       
    '[:find ?e :in $ $ ?x :where [?e]]
    "Vars used in :in should be distinct"
       
    '[:find ?e :in ?x $ ?x :where [?e]]
    "Vars used in :in should be distinct"

    '[:find ?e :in $ % ?x % :where [?e]]
    "Vars used in :in should be distinct"
       
    '[:find ?n :with ?e ?f ?e :where [?e ?f ?n]]
    "Vars used in :with should be distinct"
       
    '[:find ?x :where [$1 ?x]]
    "Where uses unknown source vars: [$1]"
       
    '[:find ?x :in $1 :where [$2 ?x]]
    "Where uses unknown source vars: [$2]"
       
    '[:find ?e :where (rule ?e)]
    "Missing rules var '%' in :in"
    ))
