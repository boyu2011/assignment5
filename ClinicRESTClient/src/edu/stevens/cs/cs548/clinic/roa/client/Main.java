//
//	Main.java
//	Client for REST service.
//					BoYu
//

package edu.stevens.cs.cs548.clinic.roa.client;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class Main {
	
	private static URI getBaseURI()
	{
		// For local uri.
		return UriBuilder.fromUri("http://localhost/").port(9090).build();
		// For EC2 uri.
		//return UriBuilder.fromUri("http://ec2-184-73-98-162.compute-1.amazonaws.com/").port(9090).build();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		
		//
		// access the service through URI
		//
		
		WebResource service = client.resource(getBaseURI()).path("clinicresource");
		
		//
		// 1. Obtaining a list of URIs for patient resources, given a patient name and date of birth.
		//	  GET /clinic/rest/patient?name=name&dob=birthdate HTTP/1.1
		//
		
		String s = ""; 
		/*
		s = service.path("getpatientbynameandbirth")
			.queryParam("name", "Tom")
			.accept(MediaType.TEXT_XML)
			.get(String.class);
		System.out.println(s);
		*/
		
		//
		// 2. Obtaining a single patient representation, given a patient resource URI. An HTTP
		//	  response code of 403 (“Not found”) occurs if there is no patient for the specified
		//	  patient identifier.
		//
		
		s = service.path("getpatientbyid")
				.queryParam("id", "3")
				.accept(MediaType.TEXT_XML)
				.get(String.class);
		System.out.println(s);
		
		//
		// 3. Obtaining a list of provider URIs, given a provider name.
		//
		
		
		//
		// 4. Obtaining a single provider representation, given a provider NPI.
		//	  An HTTP response code of 403 (“Not found”) occurs if there is no 
		//	  provider for the specified NPI.
		//
		
		s = service.path("getproviderbynpi")
				.queryParam("npi", "007")
				.accept(MediaType.TEXT_XML)
				.get(String.class);
		System.out.println(s);
		
		//
		// 5. Obtaining a treatment representation given a treatment URI. Recall
		//	  that the patient and provider representations include URIs for the 
		//    treatments to which they are related. Requests for treatment information
		//	  are based on those treatment URIs. In practice, because of latency, 
		//	  it may be useful to provide an additional operation that returns a 
		//	  representation for the list of all of the treatments for a patient, 
		//	  but you are not asked to implement this.
		//
		
		
		
		//
		// 6. Adding a patient to a clinic. The operation takes a patient representation (with no
		//	  links to treatments) and returns a new patient URI in the response header. The
		//	  HTTP response code should be 201 (“Created”), with the URI for the new patient
		//	  resource in the Location response header.
		//
		
		s = service.path("addpatient")
				.queryParam("name", "user001")
				.queryParam("age", "22")
				//.queryParam("birth", "1")
				.accept(MediaType.TEXT_XML)
				.get(String.class);
		System.out.println(s);
		
		//
		// 7. Adding a provider to a clinic. The operation takes a provider representation (with
		//	  no links to treatments) as an input representation.
		//
		
		s = service.path("addprovider")
				.queryParam("name", "newprovider1")
				.queryParam("npi", "999")
				.accept(MediaType.TEXT_XML)
				.get(String.class);
		System.out.println(s);
		
		
		//
		// 8. Adding a treatment for a patient. Define this as a POST operation on the resource
		//	  for that patient. The input representation is a treatment representation that
		//	  includes links to both the patient and provider resources for that treatment.
		//
		
		
		
	}

}
