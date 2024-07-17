package com.noomtech.dynamicdeployment.servicefactory;

/**
 * This class holds an instance of a service which is set by {@link #setJar(String, String)}, and provides a wrapper to access its end-points and also to
 * facilitate the instance of that service being updated when it's redeployed.
 * Its purpose is to decouple the service object itself, which can changes with each deployment, from its clients.
 * @author Joshua Newman
 */
interface ServiceContainer {

    void setJar(String jarUrl, String starterClass) throws Exception;
}
