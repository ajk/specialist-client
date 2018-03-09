(ns specialist-client.type-test
  (:require #?(:clj  [clojure.test :refer :all]
               :cljs [cljs.test :refer-macros [deftest is testing]])
            [clojure.spec.alpha :as s]
            [specialist-client.type :as t :include-macros true]))

#?(:cljs
  (s/def ::string t/string))

#?(:cljs
  (s/def ::int t/int))

#?(:cljs
  (s/def ::long t/long))

#?(:cljs
  (s/def ::float t/float))

#?(:cljs
  (s/def ::boolean t/boolean))


#?(:cljs
  (s/def ::id t/id))

#?(:cljs
   (deftest type-test

     (testing "valid string"
       (is (s/valid? ::string "ok")))
     (testing "invalid string"
       (is (not (s/valid? ::string nil))))

     (testing "valid int"
       (is (s/valid? ::int 1)))
     (testing "invalid int"
       (is (not (s/valid? ::int "4294967296"))))

     (testing "valid long"
       (is (s/valid? ::long "4294967296")))
     (testing "invalid long"
       (is (not (s/valid? ::long "not long"))))

     (testing "valid float"
       (is (s/valid? ::float 1.0)))
     (testing "invalid float"
       (is (not (s/valid? ::float nil))))

     (testing "valid boolean"
       (is (s/valid? ::boolean "TRUE")))
     (testing "invalid boolean"
       (is (not (s/valid? ::boolean "?"))))

     (testing "valid id"
       (is (s/valid? ::id 123)))
     (testing "invalid id"
       (is (not (s/valid? ::id [123]))))

     ))
