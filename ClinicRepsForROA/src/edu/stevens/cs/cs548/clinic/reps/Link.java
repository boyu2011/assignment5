package edu.stevens.cs.cs548.clinic.reps;

import java.net.URI;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="link", namespace=Representation.DAP_NAMESPACE)
public class Link {

	@XmlAttribute
	public URI url;
	
	@XmlAttribute
	public URI relation;
	
	@XmlAttribute
	public String mediaType;
}
