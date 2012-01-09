package edu.stevens.cs.cs548.clinic.domain;

import java.util.List;

public interface IProviderDAO {

	public List<Provider> getProvider(String name);
	
	public Provider getProviderByNPI(String npi);
	
	public Provider getProvider(long id);
	
	public void addProvider(Provider provider);
	
}
