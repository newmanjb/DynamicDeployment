# Microservices vs Monoliths in Java - Can we get the Best of Both Worlds _and_ Keep it Simple?


## Overview

At the time of writing (July '24) there's been an obsession with the micro-service architecture that's been going on for some years.

It's a good idea but it's not applicable in every situation, or for every team, or even for every organisation.

If a system is only managed by one or two teams, or regardless of how critical it is it doesn't handle massive amounts of transactions, then splitting it all up into separate services can introduce a head-ache for 
dependency management, devops and production support with very little gain.

Regardless of transaction volume though, nobody wants a monolith that contains a rat's nest of dependencies and tight coupling either, where the resulting release frequency is on a par with solar eclipses.

So I wrote this small java application to investigate the possibility of developing a monolithic application, but where each module is still decoupled to the point where they can be released independently with no downtime of the monolith, and where 
the modules can still be worked on independently by a separate team if neccessary, but where they won't have to worry about maintaining and monitoring a separate messaging layer like a REST API to other components as you would have to in a micro-service architecture.

I wanted to see if I could do this without complicating things by using something like OSGI or another framework, so as development teams could just pick up the monolith seamlessly.



## Application

There's a central hub which I've called the "ServiceFactory" which acts as a repository for each service.  A "service" in this context is what would normally be termed as a "microservice" in the microservice architecture.
	
Each service implements a service interface which is specific to the functionality it provides to clients. 

Each service instance sits within a "service container", which is just a wrapper class that implements that same service interface and who's purpose is to hide the service instance itself from clients.

Therefore client functionality within the monolith can obtain any service they want, where they will just see the methods made available from the service interface.

But the deployment mechanism is able to update any of these service objects at runtime by providing details of a newly built jar file for a service to the service factory.  The container referenced by the service factory will then use reflection 
to construct a new instance of its service object based on the jar file provided, and this will all be invisible to the client modules.

The diagram below illustrates this:


![Example Overview Diagram](DynamicDeployment1.PNG)




The code itself is an implementation of the diagram, where the "Deployment Service" in this case is something that allows the user to rebuild the jar files for each service and to see the resulting output, 
while never having to restart the application.

The code documentation contains detailed explanations but there's nothing sophisticated going on here that anyone couldn't have done over 10 years ago.  It's just using dynamic class-loading, reflection, and taking advantage of java's 
access modifiers (package-private, public and so on).


## How To Run

The repository includes 3 projects-

**1** The main application that brings services A, B and C together in a service factory and which takes the aforementioned user input.

**2** The service interfaces.

**3** Simple service implementations.

Open the projects and build the jar files, and then provide the URLs of the JAR files from project 3 to the main application in project 1 as parameters.  When the application prompts you, make some changes to the service implementations and rebuild the jars, then press enter again.  
You'll see your changes illustrated in the output and will be able to rebuild and redeploy over and again without restarting the application.

See the java docs on the "Main" class for example input parameters.




## Pros and Cons

### Cons

- Harder to scale.  You could certainly have >1 service in a container and load-balance between them, but because they're all part of the same process they'd all have to run on the same server.  
You could put different monoliths on different servers and configure them with different services, but it's a bit more complex.

- The service jars must be "fat-jars" i.e. they should contain all the necessary dependencies, otherwise it makes loading up all the class files more complex.  This could get unwieldy if the jars got too large.

- There's nothing stopping anyone from passing a mutable object over the interfaces e.g. a collection of some sort, that introduced a link between services.  This could casue havoc, especially after deployment.  Code reviews would need 
to be in place to ensure this didn't happen e.g. that only Strings or primitives were used.

- A mechanism would need to be put in place that changes the state of the container somehow while the service instance is being replaced in order to prevent requests to the service during this time e.g. a Mutex could be added, requests could be queued, or an exception could be thrown.


### Pros

- No down-time during deployments

- Complete separation of the modules/services in the code

- One single process is much easier to monitor and manage.

- It's much easier to set up different UAT and QA environments

- It's much easier to test, as it's easier to mock different components.  

- Following on from the above, running the entire system on developer's machines would be far less cumbersome, and if neccessary it wouldn't be a problem to disable some services or mock them if hardware resources were an issue.

- No strain on the network

- No network latency between components.


## Conclusion

It seems to work pretty well.  Pros and cons in my opinion are above.

It would be simple to take it further and add a "addOnMessageListener" method to the interface of a service that produced a message stream, so that clients of that service could use it to receive the stream.  

Clients of producer services like this could also use "preDeployment" and "postDeployment" listeners to enable them to be notified just before and immediately after a message-producing service was deployed, so as they could remove their message listeners and then add them again.

In the opposite situation, where the client of the service was delivering messages to it instead of consuming messages from it, the clients could use these hooks to establish a temporary queue of messages that weren't delivered during the deployment process and that could be delivered when notification of the deployment being completed was received.

These deployment life-cycle hooks could be managed in the central hub (the "ServiceFactory" in the context of the example above) by getting it to poll the last updated timestamp of the jar files.


