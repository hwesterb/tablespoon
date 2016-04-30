(import '(java.net Socket)
	'(java.io OutputStreamWriter))

(require '[cheshire.core :as json])

(def karamelserver {:name "localhost" :port 6667})

(defn connect [server]
	(let [socket (Socket. (:name server) (:port server))] socket))

(defn write [socket json]
	(def out (OutputStreamWriter. (.getOutputStream socket)))
	(.write out json)
	(.flush out))

(defn send-json [socket json]
	(write socket json))

(defn post-to-karamel
	[socket event]
	(let [event-json (json/generate-string event)]
		(send-json socket event-json)))

(defn connect-and-send 
	[event]
	(fn [event]
		(case (:type event)
			(let [socket (connect karamelserver)]
			(post-to-karamel socket event)
			(.close socket)))))


