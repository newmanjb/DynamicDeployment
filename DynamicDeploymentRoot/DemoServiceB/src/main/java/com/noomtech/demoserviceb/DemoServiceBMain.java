package com.noomtech.demoserviceb;

import com.noomtech.dynamicdeployment.services.DemoServiceB;

public class DemoServiceBMain implements DemoServiceB {


    private static final String TRANSFORMED_MARKER = "transformerRobotInDisguise91";


    public DemoServiceBMain() {
        System.out.println("Initialised Demo Service B");
    }


    @Override
    public String transformSomething(String something) {
        return something + TRANSFORMED_MARKER;
    }
}
