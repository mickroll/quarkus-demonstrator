package com.example;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.example.ExampleProducer.Example;


@ApplicationScoped
public class MyBeanNoLombokWithInject {

    @ExampleQualifier
    private final Example example;

    @Inject
    public MyBeanNoLombokWithInject(@ExampleQualifier final Example example) {
        this.example = example;
    }

    public Example getExample() {
        return example;
    }
}
