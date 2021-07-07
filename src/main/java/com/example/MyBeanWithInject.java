package com.example;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.example.ExampleProducer.Example;

import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class MyBeanWithInject {

    @ExampleQualifier
    private final Example example;

    // lombok generates like this:
    // @Inject
    // public MyBeanWithInject(@ExampleQualifier final Example example) {
    //     this.example = example;
    // }

    public Example getExample() {
        return example;
    }
}
