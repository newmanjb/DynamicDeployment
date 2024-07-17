package com.noomtech.dynamicdeployment;

import com.noomtech.dynamicdeployment.servicefactory.ServiceConfig;
import com.noomtech.dynamicdeployment.servicefactory.ServiceFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;


/**
 * Demo application that runs 3 services, each of which have a single method on their APIs.
 * It will use the output from service A as input to service B.  Service B transforms its input, which is then passed
 * to service C which will produce a boolean.
 * The application will then give the user the opportunity to update the source code for these services and rebuild them
 * before refreshing the jar files in {@link ServiceFactory} and running the above routine again, giving the user the opportunity to see their changes without this process that uses these
 * services having had to be stopped.
 * Users can change the logic in the services as much as they like and also add extra dependencies, as long as those dependencies are contained in the jar file
 * i.e. it should be a 'fat' jar.
 * Users should not rename the 'service name Main' starter classes for the services or rename their packages, as these are hard-coded into
 * this application.  The fully qualified names of the starter class for each service could easily be provided as 3 more parameters, but
 * it's easier to use with just 3 instead of 6.
 * The only thing that really couldn't be changed without a restart are the interfaces of any of the services.
 *
 * The services themselves are in separate projects, and the user should build them and provide the URLs of each JAR file
 * as arguments to this application e.g. 'file://localhost//C://users//simon//Technical//Code//DemoServiceC//target//DemoServiceA-1.0.jar'.
 * args[0] is the URL of the jar for DemoServiceA, then DemoServiceB, then DemoServiceC's URL as the third arg.
 * @author Joshua Newman
 */
public class Main {


    public static void main(String[] args) throws Exception {

        if(args.length != 3) {
            System.out.println("Usage: 'URL for DemoServiceA jar' 'URL for DemoServiceB jar' 'URL for DemoServiceC jar'");
            System.exit(1);
        }

        var serviceConfigA = new ServiceConfig(
                "DemoServiceA",
                    args[0],
                "com.noomtech.demoservicea.DemoServiceAMain");
        var serviceConfigB = new ServiceConfig(
                "DemoServiceB",
                args[1],
                "com.noomtech.demoserviceb.DemoServiceBMain");
        var serviceConfigC = new ServiceConfig(
                "DemoServiceC",
                args[2],
                "com.noomtech.demoservicec.DemoServiceCMain");

        var serviceFactory = new ServiceFactory(Arrays.asList(new ServiceConfig[]{serviceConfigA, serviceConfigB, serviceConfigC}));
        var demoServiceA = serviceFactory.getDemoServiceA();
        var demoServiceB = serviceFactory.getDemoServiceB();
        var demoServiceC = serviceFactory.getDemoServiceC();

        var quit = false;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))){
            while(!quit) {
                var data = demoServiceA.getSomething();
                System.out.println("Got this from demo service A: " + data);
                data = demoServiceB.transformSomething(data);
                System.out.println("Transformed to this by demo service B: " + data);
                System.out.println("Stored by demo service C: " + demoServiceC.storeSomething(data));
                System.out.println("Make your updates, build and package them, and press return to continue or 'q' to quit");

                var input = bufferedReader.readLine();
                if (!input.trim().equalsIgnoreCase("q")) {
                    serviceFactory.refreshService(serviceConfigA);
                    serviceFactory.refreshService(serviceConfigB);
                    serviceFactory.refreshService(serviceConfigC);
                } else {
                    quit = true;
                }
            }
        }
    }
}
