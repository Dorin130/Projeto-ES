package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACar 
{
    public static Set<RentACar> rentacars = new HashSet<>();

	public RentACar(String rentacarName) {
		//Constructor 
	}

	public String getName() {
		//get Name
		return "";
	}
	
	public String getCode() {
		return "";
	}

	public Object getNumberOfVehicles() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getRenting(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public Renting rent(RentACar rentacar, String drivingLicense, LocalDate begin, LocalDate end) {
		// TODO Auto-generated method stub
		return null;
	}

}
