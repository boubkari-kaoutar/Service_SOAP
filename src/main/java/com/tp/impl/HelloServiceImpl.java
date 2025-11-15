package com.tp.impl;

import com.tp.api.HelloService;
import com.tp.model.Person;
import jakarta.jws.WebService;

@WebService(
        serviceName = "HelloService",
        portName = "HelloServicePort",
        endpointInterface = "com.tp.api.HelloService",
        targetNamespace = "http://api.cxf.acme.com/"
)
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String name) {
        return "Bonjour, " + (name == null ? "inconnu" : name);
    }

    @Override
    public Person findPersonById(String id) {
        return new Person(id, "Ada Lovelace", 36); // maquette
    }
}
