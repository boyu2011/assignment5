package edu.stevens.cs.cs548.clinic.research.domain;

import javax.persistence.EntityManager;
import edu.stevens.cs.cs548.clinic.domain.Treatment;
import edu.stevens.cs.cs548.clinic.domain.TreatmentDAO;

public class DrugResearchDAO {

	private EntityManager em;
	
	public DrugResearchDAO(EntityManager em)
	{
		this.em = em;
	}
	
	public void addResearchInfo(long tid, String name)
	{	
		Drug drug = new Drug();
		TreatmentDAO td = new TreatmentDAO(em);
		Treatment treatment = td.getTreatment(tid);
		drug.setName(name);
		em.persist(drug);
	}
}
