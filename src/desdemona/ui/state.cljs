(ns desdemona.ui.state
  (:require [reagent.core :as r :refer [atom]]
            [desdemona.ui.sample-data :refer [sample-state]]))

(defonce state (atom sample-state))
