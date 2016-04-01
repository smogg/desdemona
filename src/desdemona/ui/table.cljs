(ns desdemona.ui.table
  (:require [reagent.session :as session]
            [desdemona.ui.dom :refer [button]]))

(defn get-available-keys
  [results]
  (set (mapcat keys results)))

(defn table-header
  [col-name]
  ^{:key col-name} [:th (name col-name)])

(defn table-row
  [alert]
  (let [toggled-cols-keys (session/get :toggled-cols)
        is-toggled? #(contains? toggled-cols-keys (key %))
        filtered-cols (filter is-toggled? alert)
        col-values (vals filtered-cols)]
    [:tr
     [:td.toggle-column
      [:button "t"]]
     (for [col col-values]
      ^{:key col} [:td col])]))

(defn table
  [results]
  (let [toggled-cols (session/get :toggled-cols)
        table-rows (map table-row results)]
   [:table.table
    [:thead>tr
      [:th]
      (map table-header toggled-cols)]
    (into [:tbody] table-rows)]))

(defn cols-toggler
  []
  (let [available-keys (get-available-keys (session/get :results))
        toggled-cols-keys (session/get :toggled-cols)
        is-toggled? #(contains? toggled-cols-keys (key %))
        update-cols #(session/update-in! [:toggled-cols]
                                         (fn [cols] (if (contains? cols %)
                                                        (disj cols %)
                                                        (conj cols %))))
        update-colss #(println %)]
    (into [:ul]
          (for [item available-keys]
            [:li [button {:on-click #(update-cols item)}
                  (name item)]]))))

(defn table-component
  []
  (let [results (session/get :results)]
  [:div.container-fluid
   [cols-toggler]
   [table results]]))
