(ns desdemona.ui.table
  (:require [wilson.dom :as wd]
            [reagent.session :as session]))

(def rows (:results @session/state))

(def all-keys (-> (mapcat wd/get-all-keys rows) distinct wd/prepare-keys))

(defn table-component
  []
  (let [sorted-rows (wd/sort-rows rows
                                  {:default
                                   (fn [k rows]
                                     (if (= (:table-sort-order @session/state)
                                            :asc)
                                       (sort-by k rows)
                                       (reverse (sort-by k rows))))}
                                  (:table-sort-key @session/state))
        get-new-order (fn [state k]
                        (let [sort-key (:table-sort-key @session/state)
                              sort-order (:table-sort-order @session/state)
                              swap-order {:asc :desc :desc :asc}]
                          {:table-sort-key k
                           :table-sort-order (if (= sort-key k)
                                               (swap-order sort-order)
                                               :asc)}))
        update-state-order
        #(session/swap! merge (get-new-order @session/state %))]
    [:div {:class "container-fluid"}
     [:div {:class "table-responsive"}
      (wd/with-class "table-condensed"
        (wd/table
         all-keys
         sorted-rows
         {:k->attrs (fn [k]
                      {:on-click #(update-state-order k)
                       :class (when (= k (:table-sort-key @session/state))
                                (name
                                 (:table-sort-order @session/state)))})}))]]))
