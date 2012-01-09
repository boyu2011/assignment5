package edu.stevens.cs.cs548.clinic.billing.domain;

import java.io.Serializable;
import javax.persistence.*;

import edu.stevens.cs.cs548.clinic.domain.Treatment;

/**
 * Entity implementation class for Entity: TreatmentBilling
 *
 */
@Entity

public class TreatmentBilling implements Serializable {

	
	private static final long serialVersionUID = 1L;

	public TreatmentBilling() {
		super();
	}
   
	@Id 
	@GeneratedValue
	private long id;

	private float amount;
	
	@OneToOne
	private Treatment treatment;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public Treatment getTreatment() {
		return treatment;
	}

	public void setTreatment(Treatment treatment) {
		this.treatment = treatment;
	}
}
