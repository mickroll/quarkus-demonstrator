package com.example.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.example.ExampleProducer.Example;
import com.example.MyBean;
import com.example.MyBeanNoLombok;
import com.example.MyBeanNoLombokWithInject;
import com.example.MyBeanWithInject;

import lombok.RequiredArgsConstructor;

@Path("/example")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class ExampleResource {

    private final MyBean bean;
    private final MyBeanWithInject bean2;
    private final MyBeanNoLombok bean3;
    private final MyBeanNoLombokWithInject bean4;


    @GET
    public Example example() {
        return bean.getExample();
    }
}