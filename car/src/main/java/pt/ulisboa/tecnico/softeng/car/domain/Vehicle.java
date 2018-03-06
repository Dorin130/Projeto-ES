package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class Vehicle {
	
	private String plate;
	private int kilometers;
	private RentACar rentAcar;
	private Set<Renting> rentings = new HashSet<>();
	
	public Vehicle(String plate, int kilometers, RentACar rentAcar) {
		checkArguments(plate, kilometers, rentAcar);
		this.plate = plate;
		this.kilometers= kilometers;	
		this.rentAcar = rentAcar;
	}
	
	private void checkArguments(String plate, int kilometers, RentACar rentAcar) {
		if (plate == null) {
			throw new CarException();
		}
		
		if(! plate.matches("^[A-Z0-9]{2}\\-[A-Z0-9]{2}\\-[A-Z0-9]{2}$")){
			throw new CarException();
		}

		if (kilometers < 0) {
			throw new CarException();
		}

		if (rentAcar == null) {
			throw new CarException();
		}
	}

	public int getKilometers() {
		return kilometers;
	}

	public void setKilometers(int kilometers) {
		this.kilometers = kilometers;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public RentACar getRentAcar() {
		return rentAcar;
	}

	public void setRentAcar(RentACar rentAcar) {
		this.rentAcar = rentAcar;
	}
	
	int getNumberOfRentings() {
		return this.rentings.size();
	}
	
	public void addRenting(Renting renting){
		this.rentings.add(renting);
	}
	
	public boolean isFree(LocalDate begin, LocalDate end) {
		for(Renting r : rentings) {
			if(r.getCancellation() != null) {
				continue;
			}
			if(r.conflict(begin, end)) {
				return false;
			}
		}
		return true;
	}
	
	public Renting rent(String drivingLicense, LocalDate begin, LocalDate end){
		if(drivingLicense == null || begin == null || end == null) {
			throw new CarException();
		}
		if(!this.isFree(begin, end)) {
			throw new CarException();
		}
		
		Renting renting = new Renting(this.getRentAcar(), drivingLicense, begin, end);
		this.addRenting(renting);
		return renting;
	}	
}
