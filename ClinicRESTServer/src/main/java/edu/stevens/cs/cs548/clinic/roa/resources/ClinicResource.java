package edu.stevens.cs.cs548.clinic.roa.resources;

import java.sql.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.sun.jersey.spi.resource.Singleton;

import edu.stevens.cs.cs548.clinic.domain.Clinic;
import edu.stevens.cs.cs548.clinic.domain.ClinicExn;
import edu.stevens.cs.cs548.clinic.reps.PatientRepresentation;
import edu.stevens.cs.cs548.clinic.reps.ProviderRepresentation;
import edu.stevens.cs.cs548.clinic.roa.service.ejb.ClinicEJB;
import edu.stevens.cs.cs548.clinic.roa.service.ejb.IClinicEJBRemote;

@Singleton
@Path("/clinicresource")
public class ClinicResource {

	@EJB(beanName = "ClinicEJB") 
	IClinicEJBRemote clinicEJB;
	
	public ClinicResource()
	{
		clinicEJB = new ClinicEJB();
	}

	@GET
	@Path("/sayhello")
	@Produces("text/plain")
	public String sayHello()
	{
		return "Hello~~";
	}

    @GET
    @Path("/createclinic")
    @Produces("text/plain")
    public String createClinic(@QueryParam("name") String name) throws ClinicExn
    {
    	Clinic c = clinicEJB.createClinic(name);
    	return c.getName();
    }
    
    @GET
    @Path("/getclinicbyname")
    @Produces("text/plain")
    public String getClinic(@QueryParam("name") String name)
    {
    	Clinic c = clinicEJB.getClinicByName(name);
    	return c.getName();
    }

    //
	// 1. Obtaining a list of URIs for patient resources, given a patient name and date of birth.
	//	  GET /clinic/rest/patient?name=name&dob=birthdate HTTP/1.1
	//
    
    @GET
    @Path("/getpatientbynameandbirth")
    //@Produces("text/plain")
    public Response getPatientsByNameAndBirth(@QueryParam("name") String name, 
    										@QueryParam("birth") Date birth)
    {
    	List<PatientRepresentation> prs = clinicEJB.getPatientRepsByNameAndBirth(name, birth);
    	// How to return list??
    	return Response.ok().entity(prs).build();
    }
    
    //
	// 2. Obtaining a single patient representation, given a patient resource URI. An HTTP
	//	  response code of 403 (“Not found”) occurs if there is no patient for the specified
	//	  patient identifier.
	//
    
    @GET 
    @Path("/getpatientbyid")
    //@Produces("text/plain")
    public Response getPatientById(@QueryParam("id") long id)
    {
    	PatientRepresentation pr = clinicEJB.getPatientRepresentation(id);
    	Response r = Response.ok().entity(pr).build();
    	return r;
    	
    }
    
    //
	// 3. Obtaining a list of provider URIs, given a provider name.
	//
    
    @GET
    @Path("/getproviderbyname")
    @Produces("text/plain")
    public String getProviderByName(@QueryParam("name") String name)
    {
    	List<ProviderRepresentation> prs = clinicEJB.getProviderRepsByName(name);
    	String s = new String();
    	for(ProviderRepresentation pr : prs)
    	{
    		s+=pr.name;
    		s+="\n";
    	}
    	return s;
    }
    
    //
	// 4. Obtaining a single provider representation, given a provider NPI.
	//	  An HTTP response code of 403 (“Not found”) occurs if there is no 
	//	  provider for the specified NPI.
	//
    
    @GET
    @Path("/getproviderbynpi")
    //@Produces("text/plain")
    public Response getProviderByNpi(@QueryParam("npi") String npi)
    {
    	ProviderRepresentation pr = clinicEJB.getProviderRepsByNpi(npi);
    	return Response.ok().entity(pr).build();
    }
    
    //
	// 5. Obtaining a treatment representation given a treatment URI. Recall
	//	  that the patient and provider representations include URIs for the 
	//    treatments to which they are related. Requests for treatment information
	//	  are based on those treatment URIs. In practice, because of latency, 
	//	  it may be useful to provide an additional operation that returns a 
	//	  representation for the list of all of the treatments for a patient, 
	//	  but you are not asked to implement this.
	//
	
    /*
    @GET
    @Path("gettreatmentbyid")
    public Response getTreatmentById(@QueryParam("id") long id)
    {
    }
    */
    
    //
	// 6. Adding a patient to a clinic. The operation takes a patient representation (with no
	//	  links to treatments) and returns a new patient URI in the response header. The
	//	  HTTP response code should be 201 (“Created”), with the URI for the new patient
	//	  resource in the Location response header.
	//
    
    @GET
    @Path("/addpatient")
    //@Produces("text/plain")
    public Response addPatient(@QueryParam("name") String name,
    						   @QueryParam("birth") Date birth,
    						   @QueryParam("age") int age)
    {
    	PatientRepresentation pr = clinicEJB.addPatient(name, birth, age);
    	return Response.ok().entity(pr).build();
    }
    
    //
	// 7. Adding a provider to a clinic. The operation takes a provider representation (with
	//	  no links to treatments) as an input representation.
	//
    
    @GET
    @Path("addprovider")
    //@Produces("text/plain")
    public Response addProvider(@QueryParam("name") String name,
    						    @QueryParam("npi") String npi)
    {
    	ProviderRepresentation pr = clinicEJB.addProvider(name, npi);
    	return Response.ok().entity(pr).build();
    }
    
    //
	// 8. Adding a treatment for a patient. Define this as a POST operation on the resource
	//	  for that patient. The input representation is a treatment representation that
	//	  includes links to both the patient and provider resources for that treatment.
	//
	
    @GET
    @Path("adddrugtreatment")
    public Response addDrugTreatment()
    {
    	return Response.ok().build();
    }
    
    @GET
    @Path("addsurgery")
    public Response addSurgery()
    {
    	return Response.ok().build();
    }
 
    @Path("addradiology")
    public Response addRadiology()
    {
    	return Response.ok().build();
    }
}




