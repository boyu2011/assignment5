package edu.stevens.cs.cs548.clinic.domain;


import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class PatientDAO implements IPatientDAO {

	private EntityManager em;
	
	private ITreatmentDAO treatmentDAO;
	
	public PatientDAO(EntityManager em)
	{
		this.em = em;
		treatmentDAO = new TreatmentDAO(em);
	}
	
	@Override
	public Patient getPatient(long id) {
		Patient patient = em.find(Patient.class, id);
		if ( patient != null )
		{
			patient.setTreatmentDAO(treatmentDAO);	//??? what bad thing will happen if i miss this sentence??
													// The returned patient is not completed, so we need assign
													// treatmentDAO(a member variable) to it.
		}
		return patient;
	}

	@Override
	public void addPatient(Patient patient) {
		if (patient!=null)
		{
			em.getTransaction().begin();
			em.persist(patient);
			patient.setTreatmentDAO(treatmentDAO);
			em.getTransaction().commit();
		}
	}
	
	@Override
	public List<Patient> getPatients(String name, Date dob)
	{
		TypedQuery<Patient> query = em.createNamedQuery("findByNameDob", Patient.class)
			.setParameter("name", name)
			.setParameter("birthdate", dob);
		List<Patient> patients = query.getResultList();
		for(Patient p : patients)
		{
			p.setTreatmentDAO(treatmentDAO);	//!!!
		}
		return patients;
	}

}
