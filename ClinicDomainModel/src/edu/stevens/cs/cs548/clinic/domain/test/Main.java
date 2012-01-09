package edu.stevens.cs.cs548.clinic.domain.test;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.stevens.cs.cs548.clinic.domain.Clinic;
import edu.stevens.cs.cs548.clinic.domain.ClinicExn;
import edu.stevens.cs.cs548.clinic.domain.ClinicGateway;
import edu.stevens.cs.cs548.clinic.domain.ClinicGatewayFactory;
import edu.stevens.cs.cs548.clinic.domain.Patient;
import edu.stevens.cs.cs548.clinic.domain.Provider;

public class Main {

	public static void main(String[] args) throws ClinicExn {
		
		ClinicGateway clinicGateway = ClinicGatewayFactory.getClinicGateway();
		//Clinic hobokenClinic = clinicGateway.createClinic("Hoboken");
		Clinic hobokenClinic = clinicGateway.getClinic("Hoboken");
		
		/*
		hobokenClinic.addPatient("Tom", new Date(1), 22);
		hobokenClinic.addPatient("Jim", new Date(2), 33);
		hobokenClinic.addPatient("Jack", new Date(2), 34);
		hobokenClinic.addPatient("Lucy", new Date(9), 22);
		*/

		//
		// Retrieve all the patients.
		//
		
		Set<Patient> patients = hobokenClinic.getPatients();
		Iterator<Patient> iter = patients.iterator();
		while(iter.hasNext())
		{
			Patient p = iter.next();
			//System.out.println(p.getId() + " " + p.getName());
		}
		
		//
		// Clinic service 1: obtaining a list of URIs for patient resources, given a patient name and date of birth.
		//
		
		//List<Patient> patientList = hobokenClinic.getPatient("Lucy", null);
/*		
		//
		// Clinic service 2: obtaining a single patient representation, given a patient resource URI.
		//
		
		Patient p = hobokenClinic.getPatient(2);
		System.out.println(p.getId() + " " + p.getName());
		
		//
		// Clinic service 3: obtaining a list of provider URIs, given a provider name.
		//
		
		hobokenClinic.addProvider("Dr Lili", "001");
		List<Provider> providers = hobokenClinic.getProvider("Dr Lili");
		for(Provider provider : providers)
		{
			System.out.println("Clinic service 3 : " + provider.getName());
		}
		
		//
		// Clinic service 4: obtaining a single provider representation, given a provider NPI
		//
		
		Provider pp = hobokenClinic.getProviderByNpi("001");
		System.out.println("Clinic servier 4: " + pp.getName());
		
		//
		// Clinic service 5: Obtaining a treatment representation given a treatment URI.
		//
		
		
		//
		// Clinic service 6: Adding a patient to a clinic.
		//
		
		hobokenClinic.addPatient("Lucas", null, 34);
		
		//
		// Clinic service 7: Adding a provider to a clinic.
		//
*/		
		hobokenClinic.addProvider("Dr. Bob", "007");
	
/*
		//
		// Clinic service 8: Adding a treatment for a patient.
		//
		
		Patient ppp = hobokenClinic.addPatient("Roy", new Date(22), 88);
		ppp.addDrugTreatment("ooo", 5);
		
		System.out.println("end..");
*/
		
	}

}
