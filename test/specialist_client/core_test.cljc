(ns specialist-client.core-test
  (:require #?(:clj  [clojure.test :refer :all]
               :cljs [cljs.test :refer-macros [deftest is testing]])
            [specialist-client.core :as core]))

#?(:cljs
   (deftest core-test
     (testing "client"
       (is (fn? (core/client "/graphql"))))))
