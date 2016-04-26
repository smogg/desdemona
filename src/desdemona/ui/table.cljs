(ns desdemona.ui.table
  (:require [wilson.dom :as wd]
            [reagent.session :as session]))

(defn table-component
  [state]
  (let [rows (:results @state)
        all-keys (-> (mapcat wd/get-all-keys rows) distinct wd/prepare-keys)
        sorted-rows (wd/sort-rows rows
                                  {:default
                                   (fn [k rows]
                                     (if (= (:table-sort-order @state)
                                            :asc)
                                       (sort-by k rows)
                                       (reverse (sort-by k rows))))}
                                  (:table-sort-key @state))
        get-new-order (fn [state k]
                        (let [sort-key (:table-sort-key @state)
                              sort-order (:table-sort-order @state)
                              swap-order {:asc :desc :desc :asc}
                              sort-key-desc (wd/describe-key sort-key)
                              k-desc (wd/describe-key k)]
                          {:table-sort-key k
                           :table-sort-order (if (= sort-key-desc k-desc)
                                               (swap-order sort-order)
                                               :asc)}))
        update-state-order
         #(session/swap! merge (get-new-order state %))]
    [:div {:class "container-fluid"}
     [:div {:class "table-responsive"}
      (wd/with-class "table-condensed"
        (wd/table
         all-keys
         sorted-rows
         {:k->attrs (fn [k]
                      {:on-click #(update-state-order k)
                       :class (when (= (wd/describe-key k) (wd/describe-key (:table-sort-key @state)))
                                (name
                                 (:table-sort-order @state)))})}))]]))
