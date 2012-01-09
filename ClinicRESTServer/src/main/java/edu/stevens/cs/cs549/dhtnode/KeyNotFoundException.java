package edu.stevens.cs.cs549.dhtnode;

import java.io.Serializable;

public class KeyNotFoundException extends Exception implements Serializable {
	public KeyNotFoundException (String m) {
		super(m);
	}
}
