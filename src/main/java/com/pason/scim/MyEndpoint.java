package com.pason.scim;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.springframework.stereotype.Component;

@Component
@Path("/j/hello")
public class MyEndpoint {

    @GET
    public String message() {
        return "Hello";
    }

}
