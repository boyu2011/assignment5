package edu.stevens.cs.cs548.clinic.domain;

import javax.persistence.EntityManager;

public class TreatmentDAO implements ITreatmentDAO {

	private EntityManager em;
	
	public TreatmentDAO(EntityManager em)
	{
		this.em = em;
	}
	
	@Override
	public void addTreatment(Treatment t) {
		em.getTransaction().begin();
		em.persist(t);
		em.getTransaction().commit();
	}

	@Override
	public Treatment getTreatment(long id)
	{
		return em.find(Treatment.class, id);
	}
}
