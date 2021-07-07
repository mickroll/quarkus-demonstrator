package com.example;

import javax.enterprise.context.ApplicationScoped;

import com.example.ExampleProducer.Example;

@ApplicationScoped
public class MyBeanNoLombok {

    @ExampleQualifier
    private final Example example;

    public MyBeanNoLombok(@ExampleQualifier final Example example) {
        this.example = example;
    }

    public Example getExample() {
        return example;
    }
}
