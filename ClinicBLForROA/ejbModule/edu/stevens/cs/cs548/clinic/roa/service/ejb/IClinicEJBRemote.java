package edu.stevens.cs.cs548.clinic.roa.service.ejb;
import java.sql.Date;
import java.util.List;

import javax.ejb.Remote;

import edu.stevens.cs.cs548.clinic.domain.Clinic;
import edu.stevens.cs.cs548.clinic.domain.ClinicExn;
import edu.stevens.cs.cs548.clinic.reps.PatientRepresentation;
import edu.stevens.cs.cs548.clinic.reps.ProviderRepresentation;

@Remote
public interface IClinicEJBRemote {

	public Clinic createClinic(String name) throws ClinicExn;
	
	public Clinic getClinicByName(String name);
	
	public PatientRepresentation getPatientRepresentation(long patientId);
	
	public List<PatientRepresentation> getPatientRepsByNameAndBirth(String name, Date birth);
	
	public List<ProviderRepresentation> getProviderRepsByName(String name);
	
	public ProviderRepresentation getProviderRepsByNpi(String npi);
	
	public PatientRepresentation addPatient(String name, Date birth, int age);
	
	public ProviderRepresentation addProvider(String name, String npi);
	
	public void addDrugTreatment(String drug, int dosage);
}
