(ns desdemona.cli-test
  (:require
   [desdemona.launcher.aeron-media-driver :as aeron]
   [desdemona.launcher.utils :as utils]
   [clojure.test :refer [deftest testing is]]
   [clojure.string :as s]
   [clojure.core.async :as a])
  (:import
   [java.io StringWriter]))

(deftest block-forever!-tests
  (let [ch (a/chan)]
    (with-redefs [a/chan (fn []
                           (a/put! ch ::blocked-forever)
                           ch)]
      (is (= (utils/block-forever!) ::blocked-forever)))))

(def fake-block-forever!
  (constantly ::blocked-forever))

(defn fake-exit
  [status]
  [::exited status])

(defmacro with-fake-launcher-side-effects
  "Runs body with a fake exit, block-forever! and stdout.

  This assumes you're using com.gfredericks.system-slash-exit, because
  it's _way_ easier to mock out than the alternative.

  Returns [body-value stdout]. If it would have blocked forever, body-value
  will be ::blocked-forever. If it would have exited, body-value will
  be [::exited status]."
  [& body]
  `(with-redefs [com.gfredericks.system-slash-exit/exit fake-exit
                 desdemona.launcher.utils/block-forever! fake-block-forever!]
     (let [stdout# (StringWriter.)]
       (binding [*out* stdout#]
         (let [result# (do ~@body)]
           [result# (str stdout#)])))))

(def ^:private usage-lines
  ["Usage:"
   ""
   "  -d, --delete-dirs  Delete the media drivers directory on startup"
   "  -h, --help         Display a help message"
   ""])

(def ^:private error-lines
  ["Unknown option: \"--xyzzy\""])

(deftest aeron-main-tests
  (testing "--help displays usage"
    (let [[result stdout] (with-fake-launcher-side-effects
                            (aeron/-main "--help"))]
      (is (= result [::exited 0]))
      (is (= stdout (s/join \newline usage-lines)))))
  (testing "bogus parameters display errors + usage"
    (let [[result stdout] (with-fake-launcher-side-effects
                            (aeron/-main "--xyzzy"))]
      (is (= result [::exited 1]))
      (is (= stdout (s/join \newline (concat error-lines usage-lines))))))
  (testing "regular run"
    (let [launched (atom false)]
      (with-redefs [aeron/launch-media-driver!
                    (fn [ctx]
                      (is (not (.dirsDeleteOnStart ctx)))
                      (reset! launched true))]
        (let [[result stdout] (with-fake-launcher-side-effects (aeron/-main))]
          (is @launched)
          (is (= result ::blocked-forever))
          (is (= stdout "Launched the Media Driver. Blocking forever...\n"))))))
  (testing "regular run with deleted directories"
    (let [launched (atom false)]
      (with-redefs [aeron/launch-media-driver!
                    (fn [ctx]
                      (is (.dirsDeleteOnStart ctx))
                      (reset! launched true))]
        (let [[result stdout] (with-fake-launcher-side-effects
                                (aeron/-main "-d"))]
          (is @launched)
          (is (= result ::blocked-forever))
          (is (= stdout "Launched the Media Driver. Blocking forever...\n"))))))
  (testing "bail with exception"
    (let [launched (atom false)]
      (with-redefs [aeron/launch-media-driver!
                    (fn [ctx] (throw (IllegalStateException. "yolo")))]
        (is (thrown-with-msg?
             Exception (re-pattern @#'aeron/aeron-launch-error-message)
             (with-fake-launcher-side-effects (aeron/-main))))))))