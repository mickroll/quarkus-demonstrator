package com.example;

import javax.enterprise.context.ApplicationScoped;

import com.example.ExampleProducer.Example;

import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class MyBean {

    @ExampleQualifier
    private final Example example;

    public Example getExample() {
        return example;
    }
}
