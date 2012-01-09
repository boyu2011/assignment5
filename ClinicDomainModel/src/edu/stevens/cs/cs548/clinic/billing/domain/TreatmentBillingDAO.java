package edu.stevens.cs.cs548.clinic.billing.domain;

import javax.persistence.EntityManager;

import edu.stevens.cs.cs548.clinic.domain.Treatment;
import edu.stevens.cs.cs548.clinic.domain.TreatmentDAO;

public class TreatmentBillingDAO {

	private EntityManager em;
	
	public TreatmentBillingDAO(EntityManager em)
	{
		this.em = em;
	}
	
	public void addBillingInfo(long tid, float amount)
	{
		TreatmentBilling tb = new TreatmentBilling();
		TreatmentDAO td = new TreatmentDAO(em);
		Treatment treatment = td.getTreatment(tid);
		tb.setTreatment(treatment);
		tb.setAmount(amount);
		em.persist(tb);
	}
	
	public TreatmentBilling getTreatmentBilling(long treatmentId)
	{
		//TO BE DONE
		return new TreatmentBilling();
	}
}
