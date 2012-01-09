package edu.stevens.cs.cs548.clinic.reps;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import edu.stevens.cs.cs548.clinic.domain.Provider;
import edu.stevens.cs.cs548.clinic.domain.Patient;

/*
@XmlRootElement(namespace="http://www.example.org/clinic/treatment",
	name="TreatmentRepresentation")
*/

public class TreatmentRepresentation extends TreatmentType {

	public LinkType getPatientLink()
	{
		return this.getPatient();
	}
	
	public LinkType getProviderLink()
	{
		return this.getProvider();
	}
	
	
	/*
	 * BoYu's code
	 * 
	@XmlElement(name="treatment_id")
	public long id;
	
	@XmlElement
	public Provider provider;
	
	@XmlElement
	public Patient patient;
	
	public TreatmentRepresentation()
	{
	}
	
	public TreatmentRepresentation(long id, Patient patient, Provider provider)
	{
		this.id = id;
		this.patient = patient;
		this.provider = provider;
	}
	*/
}

