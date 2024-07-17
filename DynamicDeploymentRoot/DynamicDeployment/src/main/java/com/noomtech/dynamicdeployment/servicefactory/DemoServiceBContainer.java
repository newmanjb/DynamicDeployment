package com.noomtech.dynamicdeployment.servicefactory;


import com.noomtech.dynamicdeployment.services.DemoServiceB;


/**
 * As for {@link DemoServiceAContainer} except this is for {@link DemoServiceB}
 * @see DemoServiceAContainer
 */
class DemoServiceBContainer implements DemoServiceB, ServiceContainer {
    
    
    private DemoServiceB service;
    
    
    DemoServiceBContainer() {
        
    }
    
    public void setJar(String jarUrl, String starterClassName) throws Exception {
        service = (DemoServiceB)ServiceFactoryUtilities.getServiceObject(jarUrl, starterClassName);
    }
    
    @Override
    public String transformSomething(String something) throws Exception {
        return service.transformSomething(something);
    }
}
