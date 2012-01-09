package edu.stevens.cs.cs548.clinic.domain;

public interface ITreatmentDAO {
	
	public void addTreatment(Treatment t);
	
	public Treatment getTreatment(long id);
}
