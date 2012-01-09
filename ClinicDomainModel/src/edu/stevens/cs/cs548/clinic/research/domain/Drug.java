package edu.stevens.cs.cs548.clinic.research.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

import edu.stevens.cs.cs548.clinic.domain.DrugTreatment;

/**
 * Entity implementation class for Entity: Drug
 *
 */
@Entity

public class Drug implements Serializable {

	
	private static final long serialVersionUID = 1L;

	public Drug() {
		super();
	}
	
	@Id
	@GeneratedValue
	private long id;
	
	private String name;
	
	@OneToMany
	private Set<DrugTreatment> treatments;
	
	//
	// Getter/Setter
	//

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<DrugTreatment> getTreatments() {
		return treatments;
	}

	public void setTreatments(Set<DrugTreatment> treatments) {
		this.treatments = treatments;
	}
}
