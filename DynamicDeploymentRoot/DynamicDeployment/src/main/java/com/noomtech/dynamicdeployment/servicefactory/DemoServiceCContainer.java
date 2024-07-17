package com.noomtech.dynamicdeployment.servicefactory;


import com.noomtech.dynamicdeployment.services.DemoServiceC;


/**
 * As for {@link DemoServiceAContainer} except this is for {@link DemoServiceC}
 * @see DemoServiceAContainer
 */
class DemoServiceCContainer implements DemoServiceC, ServiceContainer {


    private DemoServiceC service;


    DemoServiceCContainer() {

    }

    public void setJar(String jarUrl, String starterClassName) throws Exception {
        service = (DemoServiceC)ServiceFactoryUtilities.getServiceObject(jarUrl, starterClassName);
    }

    @Override
    public boolean storeSomething(String something) throws Exception {
        return service.storeSomething(something);
    }
}
