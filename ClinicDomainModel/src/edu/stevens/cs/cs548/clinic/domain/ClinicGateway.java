package edu.stevens.cs.cs548.clinic.domain;

public class ClinicGateway {

	private IClinicDAO clinicDAO = new ClinicDAO();
	
	public Clinic createClinic(String name) throws ClinicExn
	{
		Clinic clinic = new Clinic(name);
		clinicDAO.addClinic(clinic);
		return clinic;
	}
	
	public Clinic getClinic(String name)
	{
		return clinicDAO.getClinic(name);
	}
}
