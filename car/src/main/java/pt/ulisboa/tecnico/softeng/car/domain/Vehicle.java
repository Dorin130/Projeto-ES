package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
	
	public void addRenting(Renting renting){
		this.rentings.add(renting);
	}
	
	
}
