package edu.stevens.cs.cs548.clinic.roa.service.ejb;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;

import edu.stevens.cs.cs548.clinic.reps.PatientRepresentation;
import edu.stevens.cs.cs548.clinic.reps.ProviderRepresentation;
import edu.stevens.cs.cs548.clinic.reps.TreatmentRepresentation;
import edu.stevens.cs.cs548.clinic.domain.*;

/**
 * Session Bean implementation class ClinicEJB
 */
@Stateless(name="ClinicEJB")
public class ClinicEJB implements IClinicEJBRemote, IClinicEJBLocal {

	private ClinicGateway clinicGateway;
	private Clinic clinic;
	
    public ClinicEJB() {
    	clinicGateway = ClinicGatewayFactory.getClinicGateway();
    	clinic = clinicGateway.getClinic("Hoboken");
    }
    
    public Clinic createClinic(String name) throws ClinicExn
    {
    	clinic = clinicGateway.createClinic(name);
    	return clinic;
    }
    
    public Clinic getClinicByName(String name)
    {
    	clinic = clinicGateway.getClinic(name);
    	return clinic;
    }
     
	public List<PatientRepresentation> getPatientRepsByNameAndBirth(String name, Date birth)
	{
		List<Patient> patients = clinic.getPatient(name, birth);
		List<PatientRepresentation> prs = new LinkedList<PatientRepresentation>();
		for(Patient p : patients)
		{
			prs.add(new PatientRepresentation(p.getId(), p.getName(), p.getAge(), p.getBirthdate(), p.getTreatmentIds()));
		}
		
		return prs;
	}
    
    public PatientRepresentation getPatientRepresentation(long patientId)
    {
    	Patient patient = clinic.getPatient(patientId);
    	return new PatientRepresentation(patient.getId(), 
    									 patient.getName(), 
    									 patient.getAge(),
    									 patient.getBirthdate(),
    									 patient.getTreatmentIds());
    }
	
	public List<ProviderRepresentation> getProviderRepsByName(String name)
	{
		List<Provider> providers = clinic.getProvider(name);
		List<ProviderRepresentation> prs = new LinkedList<ProviderRepresentation>();
		for(Provider p : providers)
		{
			prs.add(new ProviderRepresentation(p.getId(), p.getName(), p.getNpi(), p.getTreatmentIds()));
		}
		return prs;
	}
	
	public ProviderRepresentation getProviderRepsByNpi(String npi)
	{
		Provider p = clinic.getProviderByNpi(npi);
		return new ProviderRepresentation(p.getId(),
										  p.getName(),
										  p.getNpi(),
										  p.getTreatmentIds());
										  
	}
	
	public PatientRepresentation addPatient(String name, Date birth, int age)
	{
		Patient p = clinic.addPatient(name, birth, age);
		return new PatientRepresentation(p.getId(),
										 p.getName(),
										 p.getAge(),
										 p.getBirthdate());
	}
	
	public ProviderRepresentation addProvider(String name, String npi)
	{
		Provider p = clinic.addProvider(name, npi);
		return new ProviderRepresentation(p.getId(),
										  p.getName(),
										  p.getNpi());
	}

	public void addDrugTreatment(String drug, int dosage)
	{
	
	}
	/*
	public TreatmentRepresentation getTreatment(long id)
	{
	}
	*/
}
