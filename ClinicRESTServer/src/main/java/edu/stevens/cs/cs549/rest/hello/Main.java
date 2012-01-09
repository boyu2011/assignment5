package edu.stevens.cs.cs549.rest.hello;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.PropertyConfigurator;
import org.glassfish.grizzly.http.server.HttpServer;

import com.sun.jersey.api.container.grizzly2.GrizzlyWebContainerFactory;

public class Main {

	private static String serverPropsFile = "/server.properties";
	private static String loggerPropsFile = "/log4j.properties";
	
	private int httpPort;

	private Main() {
    	try {
            PropertyConfigurator.configure(getClass().getResource(loggerPropsFile));
    		/*
    		 * Load server properties.
    		 */
    		Properties props = new Properties();
    		InputStream in = getClass().getResourceAsStream(serverPropsFile);
    		props.load(in);
    		in.close();
         	httpPort = Integer.parseInt((String)props.get("server.port.http"));
         	BASE_URI = getBaseURI();
    	
    	} catch (java.io.FileNotFoundException e) {
    		System.err.println ("Server error: "+serverPropsFile+" file not found.");
    	} catch (java.io.IOException e) {
    		System.err.println ("Server error: IO exception.");
    	} catch (Exception e) {
    		System.err.println ("Server exception:"+e);
    		e.printStackTrace();
    	}	
    }
	
	private static int getPort(int defaultPort) {
        String port = System.getProperty("jersey.test.port");
        if (null != port) {
            try {
                return Integer.parseInt(port);
            } catch (NumberFormatException e) {
            }
        }
        return defaultPort;        
    } 
    
    private URI getBaseURI() {
    	// For local.
        return UriBuilder.fromUri("http://localhost/").port(getPort(httpPort)).build();
    	// For EC2.
        //return UriBuilder.fromUri("http://ec2-184-73-98-162.compute-1.amazonaws.com/").port(getPort(httpPort)).build();
    }

    public URI BASE_URI;

    protected HttpServer startServer() throws IOException {
        
    	final Map<String, String> initParams = new HashMap<String, String>();

    	//
        // The servlet initialization parameters.
        //
    	//initParams.put("com.sun.jersey.config.property.packages", 
        //    "edu.stevens.cs.cs549.dht.resources");
    	initParams.put("com.sun.jersey.config.property.packages", "edu.stevens.cs.cs548.clinic.roa.resources");   	
        
        System.out.println("Starting grizzly...");
        return GrizzlyWebContainerFactory.create(BASE_URI, initParams);
    }
    
    
    public static void main(String[] args) throws IOException {
    	
    	Main main = new Main();
        HttpServer httpServer = main.startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nTry out %sdhtresource/sayhi\nHit enter to stop it...",
                main.BASE_URI, main.BASE_URI));
        System.in.read();
        httpServer.stop();
    }  
}
