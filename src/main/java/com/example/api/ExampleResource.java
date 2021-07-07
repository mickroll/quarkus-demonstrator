package com.example.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.example.ExampleProducer.Example;
import com.example.MyBean;

import lombok.RequiredArgsConstructor;

@Path("/example")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class ExampleResource {

    private final MyBean bean;

    @GET
    public Example example() {
        return bean.getExample();
    }
}