(ns specialist-client.test-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [specialist-client.core-test]
            [specialist-client.type-test]))

(doo-tests 'specialist-client.core-test
           'specialist-client.type-test)
