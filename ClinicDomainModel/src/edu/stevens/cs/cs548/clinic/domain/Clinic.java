package edu.stevens.cs.cs548.clinic.domain;

import java.io.Serializable;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Entity implementation class for Entity: Clinic
 *							
 *										BoYu
 */
@Entity
@Table(name="CLINIC")

@NamedQuery(
	name = "findClinicByName",
	query= "select c from Clinic c where c.name = :name")

public class Clinic implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String name;
	
	@OneToMany(mappedBy="clinic")
	private Set<Patient> patients;
	
	@OneToMany(mappedBy="clinic")
	private Set<Provider> providers;
	
	private static final long serialVersionUID = 1L;

	// Do not persistent.
	@Transient
	private IPatientDAO patientDAO;
	
	@Transient
	private IProviderDAO providerDAO;

	public Clinic() {
		super();
	}
	
	public Clinic(String name)
	{
		super();
		this.setName(name);
	}
	
	//
	// Getter/Setter
	//
	
	public long getId()
	{
		return id;
	}
	
	public void setId(long id)
	{
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
	
	public Set<Patient> getPatients()
	{
		return this.patients;
	}
	
	// Only available for sub-class.
	
	protected void SetPatients(Set<Patient> patients)
	{
		this.patients = patients;
	}
	
	public Set<Provider> getProviders()
	{
		return this.providers;
	}
	
	public void setProviders(Set<Provider> providers)
	{
		this.providers = providers;
	}
	
	public Patient addPatient(Patient patient)
	{	
		// Update the object status.
		// when we modify the set of patients, let the JPA notify the database.
		// Sync.
		Set<Patient> patients = this.getPatients();
		patients.add(patient);			// Add patient
		if ( patient.getClinic() != this )	
		{
			patient.setClinic(this);	// Keep bidirection.
		}
		this.SetPatients(patients);
		
		// Store into db.
		patientDAO.addPatient(patient);
		
		return patient;
	}
	
	public Patient addPatient(String name, Date dob, int age)
	{
		return this.addPatient(new Patient(name, dob, age));
	}
	
	public void setPatientDAO(IPatientDAO patientDAO)
	{
		this.patientDAO = patientDAO;
	}
	
	public Patient getPatient(long id)
	{
		Patient patient = patientDAO.getPatient(id);
		if(patient!=null && patient.getClinic()==this)
		{
			return patient;
		}
		else
		{
			return null;
		}
	}
	
	public List<Patient> getPatients(String name, Date dob)
	{
		// why we don't get patient from this.patients??
		List<Patient> patients = patientDAO.getPatients(name, dob);
		return patients;
	}
	
	public void setProviderDAO(IProviderDAO providerDAO)
	{
		this.providerDAO = providerDAO;
	}
	
	public Provider addProvider(Provider provider)
	{
		Set<Provider> providers = this.getProviders();
		providers.add(provider);
		if(provider.getClinic()!=this)
		{
			provider.setClinic(this);	// Keep bidirection.
		}
		this.setProviders(providers);
		
		// Store into DB.
		providerDAO.addProvider(provider);
		
		return provider;
	}
	
	public Provider addProvider(String name, String npi)
	{
		Provider p = addProvider(new Provider(name, npi));
		return p;
	}
	
	public Provider getProvider(long id)
	{
		return providerDAO.getProvider(id);
	}
	
	public List<Provider> getProvider(String name)
	{
		return providerDAO.getProvider(name);
	}
	
	public Provider getProviderByNpi(String npi)
	{
		return providerDAO.getProviderByNPI(npi);
	}
	
}
