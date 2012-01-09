package edu.stevens.cs.cs548.clinic.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;


public class ClinicDAO implements IClinicDAO {

	private EntityManager em;
	private IPatientDAO patientDAO;
	private IProviderDAO providerDAO;
	
	public ClinicDAO()
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("ClinicDomainModel");
		this.em = emf.createEntityManager();
		this.patientDAO = new PatientDAO(em);
		this.providerDAO = new ProviderDAO(em);
	}
	
	@Override
	public void addClinic(Clinic clinic) throws ClinicExn
	{
		if (getClinics(clinic.getName()).size()==0)
		{
			em.getTransaction().begin();
			em.persist(clinic);
			em.getTransaction().commit();
			clinic.setPatientDAO(this.patientDAO);		//why??
			clinic.setProviderDAO(this.providerDAO);	//why??
		}
		else
		{
			throw new ClinicExn("Duplicate clinic: "+clinic.getName());
		}
	}
	
	private List<Clinic> getClinics(String name)
	{
		try
		{
			TypedQuery<Clinic> query = em.createNamedQuery("findClinicByName", Clinic.class)
				.setParameter("name", name)
				.setMaxResults(1);
			List<Clinic> clinics = query.getResultList();
			return clinics;
		}
		catch(RuntimeException e)
		{
			throw e;
		}
	}
	
	@Override
	public Clinic getClinic(String name) 
	{
		List<Clinic> clinics = getClinics(name);
		if ( clinics.size() == 1 )
		{
			Clinic clinic = clinics.get(0);
			clinic.setPatientDAO(patientDAO);	// ???
			clinic.setProviderDAO(providerDAO);	// ???
			
			// Test Begin ---------------------
			// After loaded clinic from db, are the relevant patients loaded automatically??
			/*
			System.out.println("Test code");
			Set<Patient> patients = clinic.getPatients();
			Iterator<Patient> iter = patients.iterator();
			while(iter.hasNext())
			{
				Patient p = iter.next();
				System.out.println(p.getId() + " " + p.getName());
			}
			System.out.println("Text code end");
			*/
			// Test End ------------------------
			
			return clinic;
		}
		else 
		{
			return null;
		}
	}

}
