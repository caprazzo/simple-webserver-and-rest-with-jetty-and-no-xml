# Simple webserver and REST with jetty and NO XML

This is a very simple RESTful web service implemented using jetty embedded. I wrote this to answer the pressing question: do I need a container a a ton of xml to get anything done with java and the web? 

The answer is.... **NO**.

It actually took three classes, 15 minutes and about 150 lines of code to implement: 

* a very (very) basic in-memory key-value store with a REST API 
* a simple static file server
* a unique id generator

#### Benchmarks

I gave the thing 8m memory (-Xmx8m - yes, megabytes) and tried to push it with apache ab:
    
    # 2000 threads doing 50000 put to the store (always same key) 
    ab -u data.txt -c 2000 -n 50000 http://localhost:8180/store/somekey
    
    Time taken for tests:   30.371 seconds
    Requests per second:    1646.31 [#/sec] (mean)    
    

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

## compile and run

### with maven

    mvn assembly:assembly
    java -jar target/simple-jetty-rest-1.0-SNAPSHOT-jar-with-dependencies.jar

### with eclipse

    mvn eclipse:eclipse
    in eclipse file->import->existing project...
    then run main



