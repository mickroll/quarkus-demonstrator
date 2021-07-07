package com.example;

import javax.enterprise.context.ApplicationScoped;

import com.example.ExampleProducer.Example;

import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class MyBean {

    @ExampleQualifier
    private final Example example;

    // lombok generates like this:
    // public MyBean(@ExampleQualifier final Example example) {
    //     this.example = example;
    // }

    public Example getExample() {
        return example;
    }
}
