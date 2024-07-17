package com.noomtech.demoservicea;

import com.noomtech.dynamicdeployment.services.DemoServiceA;

public class DemoServiceAMain implements DemoServiceA {


    public DemoServiceAMain() {
        System.out.println("Initialised Demo service A");
    }

    @Override
    public String getSomething() { return Long.toString(39); }
}
