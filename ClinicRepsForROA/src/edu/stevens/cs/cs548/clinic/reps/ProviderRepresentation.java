package edu.stevens.cs.cs548.clinic.reps;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace="http://www.example.org/clinic/provider",
name="ProviderRepresentation")


public class ProviderRepresentation {

	@XmlElement(name="patient_id")
	public long id;
	
	@XmlElement
	public String name;
	
	@XmlElement
	public String npi;
	
	@XmlElement
	public List<Long> treatments;
	
	public ProviderRepresentation()
	{
		
	}
	
	public ProviderRepresentation(long id, String name, String npi)
	{
		this.id = id;
		this.name = name;
		this.npi = npi;
	}
	
	public ProviderRepresentation(long id, String name, String npi, List<Long> treatments)
	{
		this.id = id;
		this.name = name;
		this.npi = npi;
		this.treatments = treatments;
	}
}
