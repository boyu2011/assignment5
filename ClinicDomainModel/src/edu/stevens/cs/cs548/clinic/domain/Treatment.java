package edu.stevens.cs.cs548.clinic.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Treatment
 *
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="TTYPE")
@Table(name="TREATMENT")

public abstract class Treatment implements Serializable {

	//
	// Data members
	//
	
	private static final long serialVersionUID = 1L;
	
	@Id
	private long id;

	@ManyToOne
	@JoinColumn(name="patient_fk", referencedColumnName="id")
	private Patient patient;
	
	@ManyToOne
	@JoinColumn(name="provider_fk", referencedColumnName="id")
	private Provider provider;

	@Column(name="TTYPE", length=2)
	private String treatmentType;

	public Treatment() {
		super();
	}
   
	public long getId()
	{
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public Patient getPatient()
	{
		return patient;
	}
	
	public void setPatient(Patient patient)
	{
		this.patient = patient;
		if ( !patient.contains(this))
		{
			patient.addTreatment(this);	// Keep bidirection.
		}
	}
	
	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	
	public String getTreatmentType() {
		return treatmentType;
	}

	public void setTreatmentType(String treatmentType) {
		this.treatmentType = treatmentType;
	}
	
	public abstract void visit(ITreatmentVisitor v);
}
