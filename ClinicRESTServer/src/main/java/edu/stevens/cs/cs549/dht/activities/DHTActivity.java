package edu.stevens.cs.cs549.dht.activities;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import com.sun.jersey.spi.resource.Singleton;

import edu.stevens.cs.cs549.dhtnode.DHT;
import edu.stevens.cs.cs549.dhtnode.IDHT;
import edu.stevens.cs.cs549.dhtnode.KeyNotFoundException;

@Singleton
public class DHTActivity {
	
	private DHT dht;
	
	public DHTActivity() throws RemoteException
	{
		//
		// create a new DHT with key = 1.
		//
		dht = new DHT(1);
	}
	
	public void Insert(long key) throws RemoteException
	{
		IDHT node = DHT.create(key);
		dht.insert(key, node);
	}
	
	public void Delete(long key) throws RemoteException, KeyNotFoundException
	{
		dht.delete(key);
	}
	
	public List<String> GetValuesByKey(long key) throws RemoteException, KeyNotFoundException
	{	
		LinkedList<String> l = new LinkedList<String>();
		for ( String s : dht.get(key))
			l.add(s);
		return l;
	}
	
	public void Put(long key, String value) throws RemoteException 
	{
		dht.put(key, value);
	}
	
	public void Drop (long key) throws RemoteException
	{
		dht.drop(key);
	}
	
	public String SayHi()
	{
		String s = null;
		try {
			s = dht.sayHi();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
}
