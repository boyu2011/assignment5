package edu.stevens.cs.cs548.clinic.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class ProviderDAO implements IProviderDAO {

	private EntityManager em;
	
	private ITreatmentDAO treatmentDAO;
	
	public ProviderDAO(EntityManager em)
	{
		this.em = em;
		treatmentDAO = new TreatmentDAO(em);
	}
	
	@Override
	public List<Provider> getProvider(String name)
	{		
		TypedQuery<Provider> query = em.createNamedQuery("findProviderByName", Provider.class)
			.setParameter("name", name);
		List<Provider> providers = query.getResultList();
		for(Provider p : providers)
		{
			if(p!=null)
			{
				p.setTreatmentDAO(treatmentDAO);
			}
		}
		
		return providers;
	}
	
	@Override
	public Provider getProviderByNPI(String npi)
	{
		TypedQuery<Provider> query = em.createNamedQuery("findProviderByNPI", Provider.class)
			.setParameter("npi", npi);
		Provider p = query.getSingleResult();
		p.setTreatmentDAO(treatmentDAO);
		return p;
	}
	
	@Override
	public Provider getProvider(long id)
	{
		Provider provider = em.find(Provider.class, id);
		if(provider!=null)
		{
			provider.setTreatmentDAO(treatmentDAO);
		}
		return provider;
	}
	
	@Override
	public void addProvider(Provider provider)
	{
		if(provider!=null)
		{
			em.getTransaction().begin();
			em.persist(provider);
			em.getTransaction().commit();
			provider.setTreatmentDAO(treatmentDAO);	//!!!
		}
	}
}
