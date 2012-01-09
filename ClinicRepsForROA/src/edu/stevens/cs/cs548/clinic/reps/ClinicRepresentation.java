package edu.stevens.cs.cs548.clinic.reps;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(namespace="http://www.example.org/clinic/",
				name="ClinicRepresentation")

public class ClinicRepresentation {

	@XmlElement(name="clinic_id")
	public long id;
	
	@XmlElement
	public String name;
	
	public ClinicRepresentation()
	{
		
	}
}
