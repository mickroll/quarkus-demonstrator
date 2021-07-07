package com.example;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class ExampleProducer {

    @Produces
    @ExampleQualifier
    Example produceExample() {
        return new Example();
    }

    public static class Example {
    }
}
