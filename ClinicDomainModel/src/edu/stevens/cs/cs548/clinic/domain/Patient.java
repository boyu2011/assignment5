package edu.stevens.cs.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;


import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.*;


/**
 * Entity implementation class for Entity: Patient
 *
 */
@Entity
@Table(name="PATIENT")
@NamedQuery(
		name = "findByNameDob",
		query = "select p from Patient p where p.name = :name or p.birthdate = :birthdate")

//@Access(FIELD)
public class Patient implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private int age;
	
	private String name;
	
	@Temporal(TemporalType.DATE)
	private Date birthdate;
	
	@OneToMany(mappedBy="patient", targetEntity = edu.stevens.cs.cs548.clinic.domain.Treatment.class)
	private Set<Treatment> treatments;
	
	@ManyToOne
	@JoinColumn(name="clinic_fk")
	private Clinic clinic;
	
	@Transient
	private ITreatmentDAO treatmentDAO;
	

	public Patient() {
		super();
	}
	
	public Patient(String name, Date birthdate, int age)
	{
		this();
		this.setName(name);
		this.setBirthdate(birthdate);
		this.setAge(age);
	}
	
	public long getId()
	{
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public int getAge()
	{
		return age;
	}
	
	public void setAge(int age)
	{
		this.age = age;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public Date getBirthdate()
	{
		return birthdate;
	}
	
	public void setBirthdate(Date birthdate)
	{
		this.birthdate = birthdate;
	}

	protected Set<Treatment> getTreatments()
	{
		return this.treatments;
	}
	
	public void setTreatments(Set<Treatment> treatments)
	{
		this.treatments = treatments;
	}
	
	
	public void setTreatmentDAO (ITreatmentDAO treatmentDAO)
	{
		this.treatmentDAO = treatmentDAO;
	}
	
	public boolean contains(Treatment t)
	{
		return this.getTreatments().contains(t);
	}
	
	protected long addTreatment(Treatment t)
	{
		treatmentDAO.addTreatment(t);
		
		Set<Treatment> ts = this.getTreatments();
		ts.add(t);
		this.setTreatments(ts);
		
		if ( t.getPatient() != this )
		{
			t.setPatient(this);	// Keep bidirection.
		}
		
		return t.getId();
	}
	
	public long addDrugTreatment(String drug, int dosage)
	{
		return this.addTreatment(new DrugTreatment(drug, dosage));
	}
	
	public void visitTreatments(ITreatmentVisitor v)
	{
		for(Treatment t : this.getTreatments())
		{
			t.visit(v);
		}
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
	
	public static class TreatmentExn extends Exception
	{
		private static final long serialVersionUID = 1L;
		public long id;
		public TreatmentExn(String msg, long id)
		{
			super(msg);
			this.id = id;
		}
	}
	
	public void visitTreatment(long id, ITreatmentVisitor v) throws TreatmentExn
	{
		Treatment t = treatmentDAO.getTreatment(id);
		if (t==null)
			throw new TreatmentExn("No such treatment in DB: "+id, id);
		else if (t.getPatient() != this)
			throw new TreatmentExn("Treatment not for this patient: "+id, id);
		else 
			t.visit(v);
	}
	
	public Clinic getClinic()
	{
		return clinic;
	}
	
	public void setClinic(Clinic clinic)
	{
		this.clinic = clinic;
		if (!clinic.getPatients().contains(this))
		{
			clinic.addPatient(this);	// Keep bidirection.
		}
	}
   
}
