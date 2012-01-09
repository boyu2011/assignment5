package edu.stevens.cs.cs548.clinic.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("D")

public class DrugTreatment extends Treatment {

	private static final long serialVersionUID = 1L;

	private String drug;
	
	private int dosage;
	
	public DrugTreatment()
	{
		super();
	}
	
	public DrugTreatment(String drug, int dosage)
	{
		this();
		this.setDrug(drug);
		this.setDosage(dosage);
		this.setTreatmentType("D");
	}
	
	protected String getDrug() {
		return drug;
	}

	protected void setDrug(String drug) {
		this.drug = drug;
	}

	protected int getDosage() {
		return dosage;
	}

	protected void setDosage(int dosage) {
		this.dosage = dosage;
	}

	@Override
	public void visit(ITreatmentVisitor v)
	{
		v.visitDrugTreatment(this.getDrug(), this.getDosage());
	}
}
