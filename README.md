# Introduction

As Requested:

  `Create a web service to manage a video library that is designed to perform and scale well. There should be web 
  services to create, update, delete, and list videos in the library. The library should be persisted to a database. 
  The library does not need to store the videos just the following attributes of the video: Title, Description, 
  Producer, and Actors. There should be a list web service to select video with a specified actor. It would be good to 
  have a program that demonstrates or test the web services.`
  
I have implemented this in Java leveraging Drop Wizard, Jetty, Hibernate, liquibase, maven and other various tools.  
This implementation can handle about 30,000 hits/minute on a single server.
There are unit tests, and instruction to exercise below.

# Overview

This project has the following

* The `VideoDAO` illustrates using the [SQL Object Queries](http://jdbi.org/sql_object_api_queries/) features in JDBI.

* All the SQL statements for use in the `VideoDAO` are located in the `Video` class.

* `migrations.xml` initializes the database prior to running the application for the first time.

* The `VideoResource` is the REST resource which uses the VideoDAO to retrieve data from the database

The module is wired up in the `initialize` function of the `VideoApplication`.

# Running The Application

* To build this, fork or clone this git repo and execute 
        
        mvn package

* This uses the h2 database, and that db needs to be populated.  run
        
        java -jar target\video-library-1.0-SNAPSHOT.jar db migrate example.yml

* Run the server

        java -jar target\video-library-1.0-SNAPSHOT.jar server example.yml
  
* create an entry
  
        curl --noproxy localhost -i -X POST -H "Content-Type: application/json" -d@video.json http://localhost:8080/video

with the contents of `video.json` as follows:

        {
            "title":"rs",
            "description":"i",
            "producer":"BBC Cymru",
            "actors":"Matt Smith"
        }

* list all entries
        
        curl --noproxy localhost -i  http://localhost:8080/video

* delete an enry
        
        curl --noproxy localhost -i -X DELETE http://localhost:8080/videos/1

* update a video
        
        curl --noproxy localhost -i -X PUT -H "Content-Type: application/json" -d@video.json http://localhost:8080/video/1

        {   "id":1,
            "title":"rs",
            "description":"i",
            "producer":"BBC Cymru",
            "actors":"Matt Smith"
        }
