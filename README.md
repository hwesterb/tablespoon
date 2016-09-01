Tablespoon is a real-time system metrics monitoring system. It uses agent-side filtering and a publish-subscribe pattern in order to minimise performance impact.
In its current version, it is for the most part, customised for [Karamel](http://karamel.io/).

# Getting started

## Installation
Installing Tablespoon can be a bit tedious since you have to install the agents on each instance that you want to monitor. 
If you are using an orchestration tool of any sort, the installation of Tablespoon could be added with just a few lines extra code. 
All the necessary files are currently hosted on [Dropbox](https://www.dropbox.com/sh/bjd3ayiq1ux5lcr/AABDu8UdWJe3ZWb0a5LVTObra), for more details check out how [Karamel](https://github.com/karamelchef/karamel/tree/autoscaling) has implemented it.

## API
The client-side API supports several features as seen in the table below.

| Parameter | Description |
| --- | --- |
| `subscriber`| An interface that receives TablespoonEvent upon arrival. |
| `groupId` | Identifier for a group of machines. |
| `machines` | A set of specific machines, can not be used in conjunction with groupId. |
| `eventType` | Can be either GROUP AVERAGE, GROUP MEDIAN or REGULAR.
| `resource` | Can be specified as one of 69 parameters, or more broadly as CPU, MEM, NET or DSK. |
| `duration` | Specifies for how long the topic will be active on agents. |
| `sendRate` | The rate at which agents send events. |
| `retrievalDelay` | An artifical delay added to prevent excessive querying. |
| `high` | A Threshold which filters events based on a percentile. |
| `low` | A secondary Threshold to create two boundaries. |
| `replace` | Is used to replace or replicate a topic. |
| `submit` | Submits the API call asynchronously. |


The API can for example be used in the following way:
```java
/* preparing resources */
Threshold t1 = new Threshold(70.0, LESS_THAN);
Resource r = new Resource(CPU);

/* first call to the API */
String uniqueId1 = api.submitter().
subscriber(subscriber).
groupId("group 1").
eventType(GROUP_AVERAGE).
resource(r).
duration(60).
sendRate(5).
high(t1).
submit();

/* second call to the API */
Threshold t2 = new Threshold(75.0, LESS_THAN);
String uniqueId2 = api.submitter().
replace(uniqueId1, true).
high(t2).
submit();
```
