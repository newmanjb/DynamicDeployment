package com.noomtech.dynamicdeployment.servicefactory;


import com.noomtech.dynamicdeployment.services.DemoServiceA;


/**
 * Implementation of {@link ServiceContainer} for {@link DemoServiceA}
 * @author Joshua Newman
 */
class DemoServiceAContainer implements DemoServiceA, ServiceContainer {


    private DemoServiceA service;


    DemoServiceAContainer() {

    }

    /**
     * Sets a new service instance e.g. as part of a deployment, which is contained in the given jar file
     * @param jarUrl URL of the jar that contains everything for the service.  This must be a 'fat-jar' that contains all dependencies.
     * @param starterClassName The entry point to start the service.  This must have a no-args constructor that synchronously performs all
     *                         necessary initialisation routines
     * @throws Exception
     */
    public void setJar(String jarUrl, String starterClassName) throws Exception {
        service = (DemoServiceA)ServiceFactoryUtilities.getServiceObject(jarUrl, starterClassName);
    }

    /**
     * Wrapper method for {@link DemoServiceA#getSomething()}
     */
    @Override
    public String getSomething() throws Exception {
        return service.getSomething();
    }
}
