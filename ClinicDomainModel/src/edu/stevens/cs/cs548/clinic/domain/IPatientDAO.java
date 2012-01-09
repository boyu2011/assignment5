package edu.stevens.cs.cs548.clinic.domain;


import java.util.Date;
import java.util.List;

public interface IPatientDAO {

	public Patient getPatient(long id);
	
	public void addPatient(Patient patient);
	
	public List<Patient> getPatients(String name, Date dob);
}
