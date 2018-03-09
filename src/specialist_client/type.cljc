(ns specialist-client.type
  #?(:cljs
     (:require-macros [specialist-client.type :refer [defscalar defenum]]))
  (:refer-clojure :exclude [int long float boolean])
  (:require [clojure.spec.alpha :as s]
            [clojure.string :as string]))

(def scalar-kind "SCALAR")
(def object-kind "OBJECT")
(def interface-kind "INTERFACE")
(def union-kind "UNION")
(def enum-kind "ENUM")
(def input-object-kind "INPUT_OBJECT")
(def list-kind "LIST")
(def non-null-kind "NON_NULL")


#?(:clj
   (defmacro defscalar
     "Defines new scalar types."
     [type-name type-meta conform-fn]
     (let [meta-map (if (map? type-meta) type-meta {:name (name type-name) :description type-meta})]
       `(def ~(vary-meta type-name
                         assoc
                         :specialist-client.type/name (:name meta-map)
                         :specialist-client.type/kind scalar-kind
                         :specialist-client.type/type-description (:description meta-map)
                         :specialist-client.type/field-description "Self descriptive.")
          (vary-meta (s/conformer ~conform-fn)
                     assoc
                     :specialist-client.type/name ~(:name meta-map)
                     :specialist-client.type/kind ~scalar-kind
                     :specialist-client.type/type-description ~(:description meta-map)
                     :specialist-client.type/field-description "Self descriptive.")))))

#?(:clj
   (defmacro defenum
     "Defines new enum types."
     [enum-name enum-meta enum-set]
     (when-not (set? enum-set)
       (throw (#?(:clj  IllegalArgumentException.
                  :cljs js/Error.)
                       "last argument must be a set")))
     (let [meta-map (if (map? enum-meta) enum-meta {:name (name enum-name) :description enum-meta})]
       `(def ~(vary-meta enum-name
                         assoc
                         :specialist-client.type/name (:name meta-map)
                         :specialist-client.type/kind enum-kind
                         :specialist-client.type/type-description (:description meta-map)
                         :specialist-client.type/field-description "Self descriptive.")
          (vary-meta ~enum-set
                     assoc
                     :specialist-client.type/name ~(:name meta-map)
                     :specialist-client.type/kind ~enum-kind
                     :specialist-client.type/type-description ~(:description meta-map)
                     :specialist-client.type/field-description "Self descriptive.")))))

#?(:cljs
   (defn field
     ([t doc] (field t doc {}))
     ([t doc opt]
      (vary-meta t
                 assoc
                 :specialist-client.type/field-description doc
                 :specialist-client.type/is-deprecated (clojure.core/boolean (:deprecated opt))
                 :specialist-client.type/deprecation-reason (:deprecated opt)))))

;;;

#?(:cljs
   (defscalar
     string
     {:name "String"
      :description
      (str "The 'String' scalar type represents textual data, represented as UTF-8 "
           "character sequences. The String type is most often used by GraphQL to "
           "represent free-form human-readable text.")}
     (fn [v]
       (cond
         (nil? v)  ::s/invalid
         (coll? v) ::s/invalid
         :else (str v)))))

#?(:cljs
   (defscalar
     int
     {:name "Int"
      :description
      (str "The 'Int' scalar type represents non-fractional signed whole numeric values. "
           "Int can represent values between -(2^31) and 2^31 - 1.")}
     (fn [v]
       (let [int-min -2147483648
             int-max  2147483647
             i (js/parseInt v 10)]
         (if (and (not (js/isNaN i)) (>= i int-min) (<= i int-max))
           i
           ::s/invalid)))))



#?(:cljs
   (defscalar
     long
     {:name "Long"
      :description
      (str "The 'Long' scalar type represents non-fractional signed whole numeric "
           "values. Long can represent values between -(2^64) and 2^64 - 1.")}
     (fn [v]
       (let [long-min -9223372036854775808
             long-max  9223372036854775807
             i (js/parseInt v 10)]
         (if (and (not (js/isNaN i)) (>= i long-min) (<= i long-max))
           i
           ::s/invalid)))))

#?(:cljs
   (defscalar
     float
     {:name "Float"
      :description
      (str "The 'Float' scalar type represents signed double-precision fractional values "
           "as specified by IEEE 754")}
     (fn [v]
       (let [f (js/parseFloat v)]
         (if-not (js/isNaN f)
           f
           ::s/invalid)))))

#?(:cljs
   (defscalar
     boolean
     {:name "Boolean"
      :description "The 'Boolean' scalar type represents 'true' or 'false'."}
     (fn [v]
       (cond
         (and (boolean? v) (= true v))  true
         (and (boolean? v) (= false v)) false
         (string/blank? v) ::s/invalid
         (and (clojure.core/string? v) (re-find #"(?i)^true$" v)) true
         (and (clojure.core/string? v) (re-find #"(?i)^false$" v)) false
         :else ::s/invalid))))

#?(:cljs
   (defscalar
     id
     {:name "ID"
      :description
      (str "The 'ID' scalar type represents a unique identifier, often used to refetch "
           "an object or as key for a cache. The ID type appears in a JSON response as a "
           "String; however, it is not intended to be human-readable. When expected as an "
           "input type, any string (such as \"4\") or integer (such as 4) input value "
           "will be accepted as an ID.")}
     (fn [v]
       (cond
         (string/blank? v) ::s/invalid
         (coll? v) ::s/invalid
         :else (str v)))))
