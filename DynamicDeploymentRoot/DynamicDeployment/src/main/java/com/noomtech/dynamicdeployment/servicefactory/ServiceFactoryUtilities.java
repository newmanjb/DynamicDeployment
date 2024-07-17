package com.noomtech.dynamicdeployment.servicefactory;

import java.net.URL;
import java.net.URLClassLoader;

class ServiceFactoryUtilities {


    /**
     * Loads the classes for a service which are contained in the given jar file and instantiates the starter class for the service.
     * The starter class must have a no-args constructor which should synchronously perform all necessary
     * initialization operations for the service so as the object returned from this method should be ready to be used as the service entry point
     */
    static Object getServiceObject(String jarUrl, String starterClass) throws Exception {
        var urlClassLoader = new URLClassLoader(new URL[]{new URL(jarUrl)});
        urlClassLoader.loadClass(starterClass);
        return Class.forName(starterClass, true, urlClassLoader).getDeclaredConstructor(null).newInstance(new Object[]{});
    }
}
