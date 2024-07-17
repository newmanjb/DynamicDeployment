package com.noomtech.dynamicdeployment.servicefactory;

import com.noomtech.dynamicdeployment.services.DemoServiceA;
import com.noomtech.dynamicdeployment.services.DemoServiceB;
import com.noomtech.dynamicdeployment.services.DemoServiceC;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Responsible for instantiating the available services, exposing their public APIs, and re-instantiating them when
 * requested.
 * @author Joshua Newman
 */
public class ServiceFactory {


    private final Map<String,ServiceContainer> serviceContainerMap = new HashMap<>();


    //Instantiate the services from the given config and store them in memory ready for retrieval
    public ServiceFactory(List<ServiceConfig> services) throws Exception {

        for(ServiceConfig serviceConfig : services) {
            var serviceClassName = serviceConfig.serviceStarterClass();
            ServiceContainer serviceContainer;
            if(serviceClassName.endsWith(".DemoServiceAMain")) {
                serviceContainer = new DemoServiceAContainer();
                serviceContainer.setJar(serviceConfig.serviceJar(), serviceConfig.serviceStarterClass());
            }
            else if(serviceClassName.endsWith(".DemoServiceBMain")) {
                serviceContainer = new DemoServiceBContainer();
                serviceContainer.setJar(serviceConfig.serviceJar(), serviceConfig.serviceStarterClass());
            }
            else if(serviceClassName.endsWith(".DemoServiceCMain")) {
                serviceContainer = new DemoServiceCContainer();
                serviceContainer.setJar(serviceConfig.serviceJar(), serviceConfig.serviceStarterClass());
            }
            else {
                throw new IllegalArgumentException("unknown service: " + serviceConfig.serviceName());
            }

            serviceContainerMap.put(serviceConfig.serviceName(), serviceContainer);
        }
    }

    //Return the container for this service
    public DemoServiceA getDemoServiceA() {return (DemoServiceA)serviceContainerMap.get("DemoServiceA");}

    //Return the container for this service
    public DemoServiceB getDemoServiceB() {return (DemoServiceB)serviceContainerMap.get("DemoServiceB");}

    //Return the container for this service
    public DemoServiceC getDemoServiceC() {return (DemoServiceC)serviceContainerMap.get("DemoServiceC");}

    //Re-instantiate the service referred to by the given config
    public void refreshService(ServiceConfig serviceConfig) throws Exception {
        serviceContainerMap.get(serviceConfig.serviceName()).setJar(serviceConfig.serviceJar(), serviceConfig.serviceStarterClass());
    }
}
