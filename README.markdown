# Simple webserver and REST with jetty and NO XML

This is a very simple RESTful web service implemented using jetty embedded. I wrote this to answer the pressing question: do I need a container a a ton of xml to get anything done with java and the web? 

The answer is.... **NO**.

It actually took three classes, 15 minutes and about 150 lines of code to implement 

* a very (very) basic in-memory key-value store with a REST API 
* a simple static file server
* a unique id generator

#### Key-Value store api
* GET /store/<key> - the value associated with key or 404
* PUT /store/<key> - put the body of the request as value of key
* DELETE /store/<key> - deleted the value or 404

#### File server
* GET / - will show htdocs/index.html
* GET /about.html - will show htdocs/about.html
* and so on

#### Unique id generator
* GET /uuid - get a fresh uuid at each hit
