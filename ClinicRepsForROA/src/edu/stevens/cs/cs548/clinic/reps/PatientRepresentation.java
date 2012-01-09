
package edu.stevens.cs.cs548.clinic.reps;


import java.net.URI;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import stevens.cs.cs548.clinic.service.dto.PatientDTO;

/*
@XmlRootElement(namespace="http://www.example.org/clinic/patient",
				name="PatientRepresentation")
*/

public class PatientRepresentation extends PatientType{
	
	public List<LinkType> getLinks()
	{
		return this.getTreatments().getLink();
	}
	
	public PatientRepresentation()
	{
		
	}
	
	public PatientRepresentation(PatientDTO dto)
	{
		this.name = dto.name;
		this.dob = dto.birthdate;
		this.age = 0;
		
		PatientType.Treatments treatments = new PatientType.Treatments();
		List<LinkType> links = treatments.getLink();
		
		for(Long tid : dto.treatments)
		{
			String treatmentURI = null;
			LinkType link = new LinkType();
			link.setUrl(treatmentURI);
			link.setRelation(Representation.RELATION_TREATMENT);
			link.setMediaType(Representation.MEDIA_TYPE);
			links.add(link);
		}
	}
	
	/*
	@XmlElement(name="patient_id")
	public long id;
	
	@XmlElement
	public String name;
	
	@XmlElement
	public int age;
	
	@XmlElement(name="birth-date")
	public Date birthdate;
	
	@XmlElement
	public List<Long> treatments;
	
	public PatientRepresentation()
	{
		
	}
	
	public PatientRepresentation(long id, String name, int age, Date birthdate)
	{
		this.id = id;
		this.name = name;
		this.age = age;
		this.birthdate = birthdate;
	}
	
	public PatientRepresentation(long id, String name, int age, Date birthdate, List<Long> treatments)
	{
		this.id = id;
		this.name = name;
		this.age = age;
		this.birthdate = birthdate;
		this.treatments = treatments;
	}
	*/

}