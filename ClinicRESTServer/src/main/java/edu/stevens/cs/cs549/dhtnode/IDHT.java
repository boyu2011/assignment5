package edu.stevens.cs.cs549.dhtnode;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IDHT extends Remote {
		
	/*
	 * Insert a node.
	 */
	public void insert (long key, IDHT node) throws RemoteException;
	/*
	 * Delete a node.
	 */
	public void delete (long key) throws KeyNotFoundException, RemoteException;
	/*
	 * Insert a value under a key.
	 */
	public void put (long key, String value) throws RemoteException;
	/*
	 * Look up a value under a key.
	 */
	public List<String> get (long key) throws KeyNotFoundException, RemoteException;
	/*
	 * Delete all values stored under a key.
	 */
	public void drop (long key) throws RemoteException;
	
	/*
	 * Simulate fail-stop failures.
	 */
	public void failStop (long key) throws RemoteException;
	
	
	public String sayHi() throws RemoteException;
	
}
