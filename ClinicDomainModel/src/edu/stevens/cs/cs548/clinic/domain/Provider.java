package edu.stevens.cs.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Provider
 *
 */

@Entity
@Table(name="PROVIDER")
@NamedQueries({
	@NamedQuery(
		name = "findProviderByName",
		query= "select p from Provider p where p.name = :name"),
	@NamedQuery(
		name = "findProviderByNPI",
		query= "select p from Provider p where p.npi = :npi")
})

public class Provider implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String name;
	
	private String npi;

	@ManyToOne
	@JoinColumn(name="clinic_fk")
	private Clinic clinic;
	
	@OneToMany(mappedBy="provider", targetEntity = edu.stevens.cs.cs548.clinic.domain.Treatment.class)
	private Set<Treatment> treatments;

	@Transient
	private ITreatmentDAO treatmentDAO;

	public Provider() {
		super();
	}
	
	public Provider(String name, String npi)
	{
		this();
		this.setName(name);
		this.setNpi(npi);
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getNpi() {
		return npi;
	}

	public void setNpi(String npi) {
		this.npi = npi;
	}
	
	public Set<Treatment> getTreatments() {
		return treatments;
	}

	public void setTreatments(Set<Treatment> treatments) {
		this.treatments = treatments;
	}
	
	public List<Long> getTreatmentIds()
	{
		List<Long> tids = new ArrayList<Long>();
		for(Treatment t : this.getTreatments())
		{
			tids.add(t.getId());
		}
		return tids;
	}
	
	public Clinic getClinic()
	{
		return clinic;
	}

	public void setClinic(Clinic clinic)
	{
		this.clinic = clinic;
		if(!clinic.getProviders().contains(this))
		{
			clinic.addProvider(this);	// Keep bidirection.
		}
	}
	
	public void setTreatmentDAO(ITreatmentDAO treatmentDAO)
	{
		this.treatmentDAO = treatmentDAO;
	}
	
}