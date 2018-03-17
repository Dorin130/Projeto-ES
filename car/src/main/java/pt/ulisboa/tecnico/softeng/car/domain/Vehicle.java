package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;


public class Vehicle {
	
	public static Set<String> plates = new HashSet<>();
	
	private String plate;
	private int kilometers;
	private RentACar rentAcar;
	private Set<Renting> rentings = new HashSet<>();
	
	public Vehicle(String plate, int kilometers, RentACar rentAcar) {
		
		checkArguments(plate, kilometers, rentAcar);
		this.plate = plate;
		this.kilometers= kilometers;	
		this.rentAcar = rentAcar;
		this.rentAcar.addVehicle(this);
		Vehicle.plates.add(plate);
	}
	
	private void checkArguments(String plate, int kilometers, RentACar rentAcar) {
		if (plate == null) {
			throw new CarException();
		}
		
		if(! plate.matches("^[A-Z0-9]{2}\\-[A-Z0-9]{2}\\-[A-Z0-9]{2}$")){
			throw new CarException();
		}
		
		for(String p : plates) {
			if(plate.equals(p)) {
				throw new CarException();
			}
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
	
	void addKilometers(int kilometers) {
		if(kilometers < 0) {
			throw new CarException();
		}
		this.kilometers += kilometers;
	}

	public String getPlate() {
		return plate;
	}

	public RentACar getRentAcar() {
		return rentAcar;
	}
	
	public int getNumberOfRentings() {
		return this.rentings.size();
	}
	
	public Set<Renting> getRentings(){
		return rentings;
	}
	
	public void addRenting(Renting renting){
		this.rentings.add(renting);
	}
	
	boolean isFree(LocalDate begin, LocalDate end) {
		for(Renting r : this.rentings) {
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
		if(begin.isAfter(end)) {
			throw new CarException();
		}
		if(!this.isFree(begin, end)) {
			throw new CarException();
		}
		
		Renting renting = new Renting(this, drivingLicense, begin, end);
		this.addRenting(renting);
		return renting;
	}
	
	public Renting getRenting(String reference) {
		for (Renting r : this.rentings) {
			if (r.getReference().equals(reference)) {
				return r;
			}
		}
		return null;
	}
}
