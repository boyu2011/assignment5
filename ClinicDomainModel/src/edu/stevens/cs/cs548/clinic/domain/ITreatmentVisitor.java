package edu.stevens.cs.cs548.clinic.domain;

import java.util.Date;
import java.util.List;

public interface ITreatmentVisitor {
	
	public void visitDrugTreatment(String drug, int dosage);
	
	public void visitSurgery(Date date);
	
	public void visitRadiology(List<Date> dates);
}
