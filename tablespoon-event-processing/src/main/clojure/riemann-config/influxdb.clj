
; (defn send-to-influxdb [])

(require '[riemann.influxdb :as influxdb])

; (def send-to-influxdb (influxdb {:version :0.9 :host "localhost" :db "my_metrics" :port 8086 }))

(defn format-to-influxdb-point 
	[event]
	(match (:tag tag)
	(influxdb/event->point-9 tag event)
	:else (influxdb/event->point-9 "untagged" event)))



(defn influxdb-send
	[event]
	(format-to-influxdb-point event)
	nil)