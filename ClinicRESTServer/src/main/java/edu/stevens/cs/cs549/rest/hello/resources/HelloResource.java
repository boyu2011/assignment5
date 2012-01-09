package edu.stevens.cs.cs549.rest.hello.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.sun.jersey.spi.resource.Singleton;

import edu.stevens.cs.cs549.rest.hello.activities.HelloActivity;

@Singleton
@Path("/hello")
public class HelloResource {

    @GET 
    @Produces("text/plain")
    public String sayHello(@QueryParam("name") String name) 
    {
        return new HelloActivity().get(name);
    }
}
