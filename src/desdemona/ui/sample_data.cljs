(ns desdemona.ui.sample-data)

(def sample-alerts
  [{:actor-country "PL"
    :name "Halo login failure"
    :actor-username "junita.fahey"
    :type "halo_login_success"
    :critical "false"
    :id "ea7c273493de680ab0042878c7c00fc"
    :actor-ip-address "38.79.255.163"
    :created-at "2016-01-06T21:56:11.657Z"
    :message
     "Halo user travis.mercier+magento logged into the Halo Portal using browser Chrome on Windows from IP address 50.56.229.15 (USA)."
    :original
     "{\"server_id\":null,\"actor_country\":\"USA\",\"actor_ip_address\":\"50.56.229.15\",\"name\":\"Halo login success\",\"type\":\"halo_login_success\",\"actor_username\":\"travis.mercier+magento\",\"critical\":false,\"id\":\"4c993600b4c011e59a750d56cb124118\",\"created_at\":\"2016-01-06T21:56:11.657Z\",\"message\":\"Halo user travis.mercier+magento logged into the Halo Portal using browser Chrome on Windows from IP address 50.56.229.15 (USA).\"}"
    :origin "cloudpassage"}
   {:actor-country "USA"
    :name "Halo login failure"
    :actor-username "travis.mercier+magento"
    :type "halo_login_success"
    :critical "true"
    :id "4c993600b4c011e59a750d56cb124118"
    :actor-ip-address "50.56.229.15"
    :created-at "2016-01-06T21:56:11.657Z"
    :message
   "Halo user travis.mercier+magento failed to log into the Halo Portal using browser Chrome on Windows from IP address 50.56.229.15 (USA)."
    :original
   "{\"server_id\":null,\"actor_country\":\"USA\",\"actor_ip_address\":\"50.56.229.15\",\"name\":\"Halo login success\",\"type\":\"halo_login_success\",\"actor_username\":\"travis.mercier+magento\",\"critical\":false,\"id\":\"4c993600b4c011e59a750d56cb124118\",\"created_at\":\"2016-01-06T21:56:11.657Z\",\"message\":\"Halo user travis.mercier+magento logged into the Halo Portal using browser Chrome on Windows from IP address 50.56.229.15 (USA).\"}"
    :origin "cloudpassage"}])

(def toggled-cols
 #{:id :name :type :critical})
; sorted set
; ordered set (third-party pkg)
; distinct

(def sample-state
  {:query nil
   :results sample-alerts
   :toggled-cols toggled-cols})
