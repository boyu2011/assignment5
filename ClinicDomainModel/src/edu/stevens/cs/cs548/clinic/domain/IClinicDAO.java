package edu.stevens.cs.cs548.clinic.domain;

public interface IClinicDAO {

	public void addClinic (Clinic clinic) throws ClinicExn;
	
	public Clinic getClinic (String name);
	
}
