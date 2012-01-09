package edu.stevens.cs.cs549.dhtnode;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class DHT extends UnicastRemoteObject implements IDHT {

	private static final long serialVersionUID = 1L;

	public static Logger log = Logger.getLogger("edu.stevens.cs.cs549.dhtnode");

	/*
	 * This key represents the min key that this node stores.
	 */
	private long key;

	/*
	 * Database of (name,value) pairs
	 */
	private Hashtable<Long, List<String>> data = new Hashtable<Long, List<String>>();

	/*
	 * Successor and predecessor pointers.
	 */

	private DHT predecessor;
	private long predKey;

	private DHT successor;
	private long succKey;

	/*
	 * ***********************************************************************
	 * We simulate failures, but clients just get a RemoteException. What if the
	 * RemoteException is caused by a legitimate network failure?
	 */
	private boolean failed = false;

	public void failStop(long key) throws RemoteException {
		if (isin(key)) {
			if (this.key == key)
				failed = true;
		} else {
			successor.failStop(key);
		}
	}

	private void checkFailed() throws RemoteException {
		if (failed)
			throw new RemoteException("Fail stop on node: " + key);
	}

	/*
	 * Every node keeps a backup of its predecessor's database.
	 */
	private Hashtable<Long, List<String>> backup = new Hashtable<Long, List<String>>();

	/*
	 * For failure recovery, a node recovers its successor's content from its
	 * successor's successor.
	 */
	private DHT succBackup;

	/*
	 * ***************************************************************************
	 * Initialization.
	 */

	public DHT(long k) throws RemoteException {
	//public DHT(long k) {
		/*
		 * Initially every node is a single network.
		 */
		this.key = k;
		this.predKey = this.succKey = this.key;
		this.predecessor = this.successor = this.succBackup = this;
	}

	public static IDHT create(long key) throws RemoteException {
		return new DHT(key);
	}

	/*
	 * Locking and unlocking for node updates.
	 */

	private Lock lock = new ReentrantLock();

	public void lock() throws RemoteException {
		lock.lock();
	}

	public void unlock() throws RemoteException {
		lock.unlock();
	}

	/*
	 * ********************************************************************************
	 * These operations allow us to get and set successor and predecessor nodes
	 * of neighbors.
	 */

	public static class NodeInfo implements java.io.Serializable {
		private static final long serialVersionUID = 1L;
		long predKey;
		DHT predNode;
		long key;
		long succKey;
		DHT succNode;

		public NodeInfo(long p, DHT pn, long k, long s, DHT sn) {
			predKey = p;
			predNode = pn;
			key = k;
			succKey = s;
			succNode = sn;
		}
	}

	public NodeInfo getNodeInfo() throws RemoteException {
		return new NodeInfo(predKey, predecessor, key, succKey, successor);
	}

	public void setSuccessor(long key, DHT succ) throws RemoteException {
		this.key = key;
		this.successor = succ;
	}

	public void setPredecessor(long key, DHT pred) throws RemoteException {
		this.key = key;
		this.predecessor = pred;
	}
	
	public void setBackup(DHT backup) throws RemoteException {
		this.succBackup = backup;
	}

	/*
	 * *******************************************************************************
	 * Local housekeeping operations.
	 */

	private boolean isin(long key) {
		/*
		 * Is the key in the range of this node? Include a special case for when
		 * there is a single node in the DHT.
		 */
		return ((this.key == this.succKey) || (this.key <= key && key < this.succKey));
	}

	public void putNoLock(long k, String v) throws RemoteException {
		/*
		 * Insert a value in the database, assuming the caller has the lock.
		 */
		List<String> vals = data.get(key);
		if (vals == null) {
			vals = new ArrayList<String>();
		}
		vals.add(v);
	}

	private void transfer(long key, DHT node) throws RemoteException {
		/*
		 * Transfer all local key-value pairs with key >= key to node. Assume
		 * that we have locked "this" and "node".
		 * 
		 * Danger, Will Robinson! If a network failure happens, the hash table
		 * is hosed. These should be wrapped in a transaction, along with the
		 * modifications of the successor and predecessor pointers.
		 */
		Iterator<Map.Entry<Long, List<String>>> map = data.entrySet()
				.iterator();
		while (map.hasNext()) {
			Map.Entry<Long, List<String>> kvPair = map.next();
			long k = kvPair.getKey();
			if (k >= key) {
				for (String v : kvPair.getValue()) {
					node.putNoLock(k, v);
				}
			}
		}
	}

	/*
	 * ****************************************************************************
	 * For insertion and deletion of nodes, need to transfer entire backup
	 * databases. We provide resetBackup() to allow our backup database to be
	 * reset to empty, and transferBackup() is called to move a node's entire
	 * backup database to another node.
	 * 
	 * Note: We assume no node failures while moving backup databases around!
	 */

	public void resetBackup() throws RemoteException {
		backup = new Hashtable<Long, List<String>>();
	}

	public void putBackup(long key, String value) throws RemoteException {
		// Add the (key,value) pair to the backup database at this node.
		List<String> vals = backup.get(key);
		if (vals == null) {
			vals = new ArrayList<String>();
		}
		vals.add(value);
		
		//
		// added by bob.
		//
		backup.put(key, vals);
	}

	public void transferBackup(DHT node) throws RemoteException {
		/*
		 * Transfer all key-value pairs to backup database on node. This is done
		 * during insertion and deletion of nodes.
		 * 
		 * Assume that we have locked "this" and "node".
		 */
		node.resetBackup();
		Iterator<Map.Entry<Long, List<String>>> map = data.entrySet()
				.iterator();
		while (map.hasNext()) {
			Map.Entry<Long, List<String>> kvPair = map.next();
			long k = kvPair.getKey();
			for (String v : kvPair.getValue()) {
				node.putBackup(k, v);
			}
		}
	}

	/*
	 * *************************************************************************
	 * Failure handling. RestoreFromBackup() is called on a backup database to
	 * insert its content into the predecessor of a node that has failed.
	 */

	private void recoverFromFailure(DHT backup) throws RemoteException {
		this.lock();
		try {
			backup.lock();
			try {
				// Merge data from backup database into this node's database
				backup.restoreFromBackup(this);
				// Copy our database to the backup. Not the most efficient,
				// we should just update the backup with our original data.
				this.transferBackup(backup);
				// Reset successor and predecessor pointers.
				NodeInfo backupInfo = backup.getNodeInfo();
				this.succKey = backupInfo.key;
				this.successor = backup;
				this.succBackup = backupInfo.succNode;
				backup.setPredecessor(key, this);
			} finally {
				backup.unlock();
			}
		} finally {
			this.unlock();
		}
	}

	public void restoreFromBackup(DHT node) throws RemoteException {
		Iterator<Map.Entry<Long, List<String>>> map = data.entrySet()
				.iterator();
		while (map.hasNext()) {
			Map.Entry<Long, List<String>> kvPair = map.next();
			long k = kvPair.getKey();
			for (String v : kvPair.getValue()) {
				node.putNoLock(k, v);
			}
		}
	}

	private void tryInsert(long key, DHT node) throws RemoteException {
		try {
			successor.insert(key, node);
		} catch (RemoteException e) {
			// We will assume the successor has failed.
			recoverFromFailure(succBackup);
			this.insert(key, node);
		}
	}

	private void tryDelete(long key) throws RemoteException,
			KeyNotFoundException {
		try {
			successor.delete(key);
		} catch (RemoteException e) {
			// We will assume the successor has failed.
			recoverFromFailure(succBackup);
			this.delete(key);
		}
	}

	private void tryPut(long key, String value) throws RemoteException {
		try {
			successor.put(key, value);
		} catch (RemoteException e) {
			// We will assume the successor has failed.
			recoverFromFailure(succBackup);
			this.put(key, value);
		}
	}

	private void tryDrop(long key) throws RemoteException {
		try {
			successor.drop(key);
		} catch (RemoteException e) {
			// We will assume the successor has failed.
			recoverFromFailure(succBackup);
			this.drop(key);
		}
	}

	private List<String> tryGet(long key) throws RemoteException,
			KeyNotFoundException {
		try {
			return successor.get(key);
		} catch (RemoteException e) {
			// We will assume the successor has failed.
			recoverFromFailure(succBackup);
			return this.get(key);
		}
	}

	/*
	 * ************************************************************************
	 * The public client interface of a DHT, offered by every node.
	 */

	public void insert(long key, IDHT node) throws RemoteException {
		this.insert(key, (DHT) node);
	}

	public void insert(long key, DHT node) throws RemoteException {
		// Add the DHT node as a successor of this node?
		this.checkFailed();
		this.lock();
		if (isin(key)) {
			// Don't allow duplicates.
			if (this.key == key) {
				this.unlock();
				return;
			}
			DHT succ = successor;
			try {
				// Special handling when we insert into a single-node network
				boolean singleNode = (this.key == succKey);
				if (!singleNode)
					succ.lock();
				try {
					node.setSuccessor(succKey, successor);
					successor.setPredecessor(key, node);
					node.setPredecessor(this.key, this);
					this.setSuccessor(key, node);

					// Transfer data with keys >= key to the new successor
					// node.
					this.transfer(key, node);

					// "This" is backed up by "node," "node" is backed up by
					// successor
					node.setBackup(this.succBackup);
					this.setBackup(succ);
					this.transferBackup(node);
					node.transferBackup(succ);
				} finally {
					if (!singleNode) succ.unlock();
				}
			} finally {
				this.unlock();
			}
		} else {
			this.unlock();
			this.tryInsert(key, node);
		}
	}

	public void delete(long key) throws RemoteException, KeyNotFoundException {
		// Delete a node with the specified node key.
		this.checkFailed();
		if (this.key == succKey)
			return;

		this.lock();
		if (isin(key) && this.key != key) {
			// No node to delete for this key.
			this.unlock();
			throw new KeyNotFoundException("No node for key: " + key);
		} else if (this.key == key) {
			predecessor.lock();
			try {
				successor.lock();
				try {
					successor.setPredecessor(predKey, predecessor);
					predecessor.setSuccessor(succKey, successor);

					// Transfer all data to the predecessor node.
					this.transfer(this.key, predecessor);

					// Set the successor to be backup for the predecessor.
					predecessor.transferBackup(successor);
				} finally {
					successor.unlock();
				}
			} finally {
				this.unlock();
				predecessor.unlock();
			}
		} else {
			this.unlock();
			this.tryDelete(key);
		}
	}

	public void put(long key, String value) throws RemoteException {
		// Add the (key,value) pair to this node?
		this.checkFailed();
		this.lock();
		if (isin(key)) {
			List<String> vals = data.get(key);
			if (vals == null) {
				vals = new ArrayList<String>();
			}
			vals.add(value);
			
			//
			// added by bob.
			//
			data.put(key, vals);
			
			// We've added the (k,v) pair, now add it to the
			// backup database on the successor.
			try {
				if (this.key == succKey)
					return;
				successor.lock();
				try {
					successor.putBackup(key, value);
				} finally {
					successor.unlock();
				}
			} finally {
				this.unlock();
			}
		} else {
			// Always go forward.
			this.unlock();
			this.tryPut(key, value);
		}
	}

	public List<String> get(long key) throws KeyNotFoundException,
			RemoteException {
		// Lookup the (key,value) pair in this node?
		this.checkFailed();
		this.lock();
		if (isin(key)) {
			try {
				List<String> vals = data.get(key);
				if (vals == null) {
					throw new KeyNotFoundException("Key value: " + key);
				} else {
					return vals;
				}
			} finally {
				this.unlock();
			}
		} else {
			// Always go forward.
			this.unlock();
			return this.tryGet(key);
		}
	}

	public void drop(long key) throws RemoteException {
		this.checkFailed();
		this.lock();
		if (isin(key)) {
			try {
				data.remove(key);
				successor.lock();
				try {
					successor.dropBackup(key);
				} finally {
					successor.unlock();
				}
			} finally {
				this.unlock();
			}
		} else {
			// Always go forward.
			this.unlock();
			this.tryDrop(key);
		}
	}

	public void dropBackup(long key) throws RemoteException {
		backup.remove(key);
	}
	
	public String sayHi() throws RemoteException
	{
		return "DHT::sayHi()";
	}

}