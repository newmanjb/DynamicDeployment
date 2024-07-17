package com.noomtech.demoservicec;

import com.noomtech.dynamicdeployment.services.DemoServiceC;

public class DemoServiceCMain implements DemoServiceC {

    public DemoServiceCMain() {
        System.out.println("Initialised Demo Service C");
    }


    @Override
    public boolean storeSomething(String something) {
        return false;
    }
}
