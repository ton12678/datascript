(ns datascript.test.parser-find
  (:require-macros
    [cemerick.cljs.test :refer [is are deftest testing]])
  (:require
    [cemerick.cljs.test :as t]
    [datascript.parser :as dp]))

(deftest test-parse-find
  (is (= (dp/parse-find '[?a ?b])
         (dp/FindRel. [(dp/Variable. '?a) (dp/Variable. '?b)])))
  (is (= (dp/parse-find '[[?a ...]])
         (dp/FindColl. (dp/Variable. '?a))))
  (is (= (dp/parse-find '[?a .])
         (dp/FindScalar. (dp/Variable. '?a))))
  (is (= (dp/parse-find '[[?a ?b]])
         (dp/FindTuple. [(dp/Variable. '?a) (dp/Variable. '?b)]))))

(deftest test-parse-aggregate
  (is (= (dp/parse-find '[?a (count ?b)])
         (dp/FindRel. [(dp/Variable. '?a) (dp/Aggregate. (dp/PlainSymbol. 'count) [(dp/Variable. '?b)])])))
  (is (= (dp/parse-find '[[(count ?a) ...]])
         (dp/FindColl. (dp/Aggregate. (dp/PlainSymbol. 'count) [(dp/Variable. '?a)]))))
  (is (= (dp/parse-find '[(count ?a) .])
         (dp/FindScalar. (dp/Aggregate. (dp/PlainSymbol. 'count) [(dp/Variable. '?a)]))))
  (is (= (dp/parse-find '[[(count ?a) ?b]])
         (dp/FindTuple. [(dp/Aggregate. (dp/PlainSymbol. 'count) [(dp/Variable. '?a)]) (dp/Variable. '?b)]))))

(deftest test-parse-custom-aggregates
  (is (= (dp/parse-find '[(aggregate ?f ?a)])
         (dp/FindRel. [(dp/Aggregate. (dp/Variable. '?f) [(dp/Variable. '?a)])])))
  (is (= (dp/parse-find '[?a (aggregate ?f ?b)])
         (dp/FindRel. [(dp/Variable. '?a) (dp/Aggregate. (dp/Variable. '?f) [(dp/Variable. '?b)])])))
  (is (= (dp/parse-find '[[(aggregate ?f ?a) ...]])
         (dp/FindColl. (dp/Aggregate. (dp/Variable. '?f) [(dp/Variable. '?a)]))))
  (is (= (dp/parse-find '[(aggregate ?f ?a) .])
         (dp/FindScalar. (dp/Aggregate. (dp/Variable. '?f) [(dp/Variable. '?a)]))))
  (is (= (dp/parse-find '[[(aggregate ?f ?a) ?b]])
         (dp/FindTuple. [(dp/Aggregate. (dp/Variable. '?f) [(dp/Variable. '?a)]) (dp/Variable. '?b)]))))

(deftest test-parse-find-elements
  (is (= (dp/parse-find '[(count ?b 1 $x) .])
         (dp/FindScalar. (dp/Aggregate. (dp/PlainSymbol. 'count)
                                          [(dp/Variable. '?b)
                                           (dp/Constant. 1)
                                           (dp/SrcVar. '$x)])))))

#_(t/test-ns 'datascript.test.find-parser)
