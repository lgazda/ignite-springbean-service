# igniteservice

Simple Ignite Spring Bean configuration with Ignite service deployment and client access. 
---
For more please visith wiki section: https://github.com/lgazda/ignite-springbean-service/wiki



--- 
# Ignite Service and Service Deployment using Spring Bean Configuration.
We will to demonstrate how you can implement, deploy and access Ignite service using Ignite Spring Bean configuration and what benefits such configuration gives.
In the example I will simulate a situation when an ignite service is deployed on 2 server nodes and is being accessed/executed from one client node. 

## Ignite Service ? Why to bother ?
As always we may ask a question should I even bother (to create a service)? and an answer is as obvious as question: it depends. 
We will try to collect few points which may give you some hint or force you to think / to consider service exposure as an option. 
Please don't treat the list below as some kind of check list or classifier also you shouldn't expose the whole of your logic as Ignite services - approach should always fit to your application architecture and to a problem. 

### When we can/should use Ignite services and what we can gain using them 
* when we want to execute a specific business logic located on the server node 
* we are hiding business logic details, client node doesn't need to know what logic/piece of code is needed to be executed on the server node
* we gain clean code and concern separation - logic is kept on the server side and client gets only a nice interface
* client side doesn’t need to think about ignite jobs creation
* client side doesn't need to know/think about job distribution / cluster selection
** service can be deployed on the specific nodes only (based on some predicate) 
** client only says: "give me the service" and execute it
* whenever we think it's the right thing 

### When not to use services or when to use plain Ignite jobs
* when we are using Ignite nodes as a compute cluster 
* when we want to broadcast some task  
* when we want to create and run dynamic tasks
* whenever we think it's the right thing

## Service Thread Pool 

If you want to use services you need to be aware that service calls are being processed by Ignite ServiceThreadPool. 
To read more about the pool and check how you can set it up or change default settings please refer to https://apacheignite.readme.io/docs/thread-pools#section-services-pool

In short: by the default the poll thread count is set to number of CPU cores and the default queue size is unlimited. 
It means that if we are running Ignite node on 12 core CPU then we will be able to run 12 concurrent service requests where other requests will be queued and will wait for it’s turn.

> Please be aware that you shouldn’t be executing service from other services because this may lead to the thread lock - due to thread pool size - there might be not free service thread to execute sub-service request so main calls will be blocked.

# Ignite Service as a part of Ignite Bean configuration

In our example we will deploy a simple ignite service where the service definition and deployment is a part of Ignite Spring Bean definition. 
Next few points will point what gains and cons such configuration can bring to our application. 

## Gains

* we can be sure that the service is already deployed when other Spring components want to access it
* we can be sure that the service is accessible from other nodes (if we are deploying service on every node without any predicate) - just after the node joins the cluster - it is also safe to get and execute Ignite Service Proxy on the client node side (a client node waits for at least one server node and if this node joins the cluster / is accessible we can be sure that the service is also there)
* a service definition/deployment is more declarative 
* we are gaining a "cleaner" code
* the service becomes an integral part of Ignite node - basically node can’t live without the service (if this is our use case - where the node logic is encapsulated in the ignite service)

## Cons 

* we are losing some flexibility around the service deployment process - if the service deployment is conditional or has some external dependencies we don't want or can't resolve during start of the Spring context   
* services can't be deployed on demand / dynamically  
* ...


# Code example

OK it's the final time to write some code. Let:


`pl.net.gazda.common.BusinessService` interface represents an application business logic and `pl.net.gazda.server.SpringBusinessService` will be its simple implementation.

```java
public interface BusinessService {
    Object someOperation();
}
``` 

```java
@Service("springService")
public class SpringBusinessService implements BusinessService {
    private final SpringSubService subService;

    @Autowired
    public SpringBusinessService(SpringSubService subService) {
        this.subService = subService;
    }

    @Override
    public Object someOperation() {
        //calls to other spring services
        return subService.igniteBasedLogic();
    }
}
```

now let’s try to expose this logic as an Ignite service so it can be accessed remotely from other nodes.

`pl.net.gazda.common.IgniteBusinessService` combines `pl.net.gazda.common.BusinessService` and `org.apache.ignite.services.Service` interfaces. 
`org.apache.ignite.services.Service` is an interface which must be implemented if we want to deploy a class as an Ignite service.

```java
public interface IgniteBusinessService extends Service, BusinessService {
    String NAME = "IgniteService";
}
```

now what is missing it’s an implementation, `pl.net.gazda.server.SimpleIgniteService` does the work:
```java
@Component
public class SimpleIgniteService implements IgniteBusinessService {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleIgniteService.class);

    @IgniteInstanceResource
    private transient Ignite ignite;

    @SpringResource(resourceName = "springBusinessService")
    private transient SpringBusinessService springBusinessService;

    @Override
    public Object someOperation() {
        LOG.info("Node: {} - delegating operation to spring service.", localNode().id());
        return springBusinessService.someOperation();
    }

    private ClusterNode localNode() {
        return ignite.cluster().forLocal().node();
    }

    @Override
    public void cancel(ServiceContext ctx) {
        LOG.info("Service is being canceled / un-deployed.");
    }

    @Override
    public void init(ServiceContext ctx) throws Exception {
        LOG.info("Node: {} = service is being initialized.", localNode().id());
    }

    @Override
    public void execute(ServiceContext ctx) throws Exception {
        LOG.info("Node: {} - service deployed.", localNode().id());
    }
}
```

basically what `SimpleIgniteService` does is that it delegates executions to `SpringBusinessService` implementation. 
```java
@Override
public Object someOperation() {
   LOG.info("Node: {} - delegating operation to spring service.", localNode().id());
   return springBusinessService.someOperation();
}
```

as you can see `springBusinessService` is a spring bean instance injected to the ignite service implementation using `SpringResource` annotation. 
```java
@SpringResource(resourceName = "springBusinessService")
private transient SpringBusinessService springBusinessService;
```
In general we can inject any spring resource (defined in the same Spring context in which deployed service was created) using special `org.apache.ignite.resources.SpringResource` annotation and specifying the resource name (spring bean name). 
Such implementation is clear and clean. Business logic can be tested in an isolation and we can say that the ignite service is in some way separated from the business requirements - where it should contain only an ignite related logic (“technical” code). 
Of course this only a recommendation and nothing and no one will stop you to do this. 

> Please note that in my implementation I used a transient modifier. Why it’s necessary to do this ? If we don’t mark `SpringBusinessService` or `Ignite` instance as `transient` Ignite will try to serialize both instances and try to send them to other nodes - and please believe me, we don’t want to do this (this will also lead to an error). 

But does it mean that after the service is automatically deployed on other node those instances will be NULL ? No - Ignite will automatically inject a correct bean during the service deployment phase. 
Now let's move to the client side. `pl.net.gazda.client.ClientService' demonstrates how the service can obtained and executed.
```java
@Scheduled(fixedRate = 1000, initialDelay = 1000)
public void simulateIgniteServiceExecution() {
    LOG.info("Executing service and getting result: " + igniteService().someOperation());
}

private IgniteBusinessService igniteService() {
    return ignite.services(getServerCluster()) //getting service from all server nodes
            .serviceProxy(IgniteBusinessService.NAME, IgniteBusinessService.class, false); //sticky session set to false
}

private ClusterGroup getServerCluster() {
    return ignite.cluster().forServers();
}
```

You can start a whole application sample by running `pl.net.gazda.SpringRunner.main()` method. It starts three separate spring contexts: 2 with server node and one client node context.
```java
public static void main(String[] args) {
   startIgniteServerContext();
   startIgniteServerContext();
   startIgniteClientContext();
}
```

in the log you can see two started and running server nodes and a client node which executes ignite service in a RoundRobin fashion against both (two) server nodes.

```
>>> Local node [ID=F515F616-0BF2-41B6-A738-A706EAC894DB, order=1, clientMode=false]
...
>>> Local node [ID=11FF099D-6E31-4D0D-A981-8505D52E16C1, order=3, clientMode=false]
...
Topology snapshot [ver=3, servers=2, clients=1, CPUs=4, heap=1.8GB]
...
INFO  pl.net.gazda.server.SimpleIgniteService - [] Node: f2b0edeb-420b-499a-84f6-f28272a3671c - service deployed.
...
INFO  pl.net.gazda.server.SimpleIgniteService - [] Node: 15827770-3474-402b-9f75-f2f22ebf9260 - service deployed.
...
INFO  pl.net.gazda.server.SimpleIgniteService - [] Node: 11ff099d-6e31-4d0d-a981-8505d52e16c1 - delegating operation to spring service.
INFO  pl.net.gazda.client.ClientService - [] Executing service and getting result: Result from 11ff099d-6e31-4d0d-a981-8505d52e16c1
...
INFO  pl.net.gazda.server.SimpleIgniteService - [] Node: f515f616-0bf2-41b6-a738-a706eac894db - delegating operation to spring service.
INFO  pl.net.gazda.client.ClientService - [] Executing service and getting result: Result from f515f616-0bf2-41b6-a738-a706eac894db
...
```

