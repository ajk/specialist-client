(ns specialist-client.core
  #?(:cljs
     (:require [specialist-client.type :as t :include-macros true])))

(def default-opt
  {:method      "post"
   :credentials "include"
   :headers     {"Accept"       "application/json"
                 "Content-Type" "application/json"
                 "X-Requested-With" "fetch"}})

#?(:cljs
   (defn default-fail [res]
     (doseq [e (:errors res)]
       (when (:message e)
         (js/console.error "error:" (:message e))))
     (-> res clj->js js/console.error)))

#?(:cljs
   (defn client
     ([url] (client url {}))
     ([url input-opt]
      (let [req-opt (merge default-opt input-opt)]
        (fn [body opt]
          (let [{:keys [on-success on-failure]} (if (fn? opt)
                                                  {:on-success opt
                                                   :on-failure default-fail}
                                                  opt)]
            (-> (js/fetch
                  url (-> req-opt
                          (assoc :body (.stringify js/JSON (clj->js body)))
                          clj->js))
                (.then
                  (fn [res]
                    (if (= 200 (.-status res))
                      (.json res)
                      (on-failure
                        {:errors [{:message "request failed"
                                   :status (.-status res)
                                   :status-text (.-statusText res)
                                   :body (.-body res)}]}))))
                (.then
                  (fn [res]
                    (let [data (js->clj res :keywordize-keys true)]
                      (if (:data data)
                        (on-success data)
                        (on-failure data)))))
                (.catch
                  (fn [err]
                    (on-failure
                      {:errors [{:message "request failed"
                                 :exception (js->clj err)}]}))))))))))
