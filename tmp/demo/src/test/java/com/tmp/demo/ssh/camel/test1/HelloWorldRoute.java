package com.tmp.demo.ssh.camel.test1;

import org.apache.camel.builder.RouteBuilder;

public class HelloWorldRoute extends RouteBuilder {


    public void configure() throws Exception {
        System.out.println("hello world camel");
    }
}