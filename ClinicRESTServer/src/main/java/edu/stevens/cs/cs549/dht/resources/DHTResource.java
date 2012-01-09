//
// Added by Bob.
// 

package edu.stevens.cs.cs549.dht.resources;

import java.rmi.RemoteException;
import java.util.LinkedList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.sun.jersey.spi.resource.Singleton;

import edu.stevens.cs.cs549.dht.activities.DHTActivity;
import edu.stevens.cs.cs549.dhtnode.KeyNotFoundException;

@Singleton
@Path("/dhtresource")
public class DHTResource {
	
    private DHTActivity dhtActivity;
    
    public DHTResource() throws RemoteException
    {
    	dhtActivity = new DHTActivity();
    }
    
    @GET
    @Path("/sayhi")
    @Produces("text/plain")
    public String sayhi()
    {
    	return dhtActivity.SayHi();
    }
  
    @GET
    @Path("/insert")
    @Produces("text/plain")
    public String insert(@QueryParam("key") long key) throws RemoteException
    {
	    dhtActivity.Insert(key);
	    return "REST insert() was called\n" + "Key = " + Long.toString(key);
    }
    
    @GET
    @Path("/delete")
    @Produces("text/plain")
    public String delete(@QueryParam("key") long key) throws RemoteException, KeyNotFoundException
    {
    	dhtActivity.Delete(key);
    	return "REST delete() was called\n" + "Key = " + Long.toString(key);
    }
    
    @GET
    @Path("/get")
    @Produces("text/plain")
    public String get(@QueryParam("key") long key) throws RemoteException, KeyNotFoundException
    {
    	LinkedList<String> l = new LinkedList<String>();
    	l = (LinkedList<String>)dhtActivity.GetValuesByKey(key);
    	String ret = null;
    	for( String s : l )
    		ret += s;
    	if ( ret == null )
    		return "Not found values for the key.";
    	else
    		return "REST get() was called.\n" + "Key = " + Long.toString(key) + " Values = " + ret;
    }
    
    @GET
    @Path("/put")
    @Produces("text/plain")
    public String put(@QueryParam("key") long key, @QueryParam("value") String value) throws RemoteException
    {
    	dhtActivity.Put(key, value);
    	return "REST put() was called.\n" + "Key = " + Long.toString(key) + " Value = " + value;
    }
    
    @GET
    @Path("/drop")
    @Produces("text/plain")
    public String drop(@QueryParam("key") long key) throws RemoteException
    {
    	dhtActivity.Drop(key);
    	return "REST drop() was called.\n" + "Key = " + Long.toString(key);
    }
}
