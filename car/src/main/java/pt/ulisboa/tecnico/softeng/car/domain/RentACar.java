package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACar {
    public static Set<RentACar> rentacars = new HashSet<>();

	private static int counter = 0;
    
    private final String name;
    private final String code;
    private final Set<Vehicle> vehicles = new HashSet<>();

	public RentACar(String name) {
		checkArguments(name);
		
		this.name = name;
		this.code = Integer.toString(++RentACar.counter);
		RentACar.rentacars.add(this);
	}

	private void checkArguments(String name) {
		if(name == null || name == "") {
			throw new CarException();
		}
	}

	public String getName() {
		return this.name;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public void addVehicle(Vehicle vehicle) {
		if (hasVehicle(vehicle.getPlate())) {
			throw new CarException();
		}
		this.vehicles.add(vehicle);
	}

	public boolean hasVehicle(String plate) {
		for (Vehicle vehicle : this.vehicles) {
			if(vehicle.getPlate().equals(plate)) {
				return true;
			}
		}
		return false;
	}

	public int getNumberOfVehicles() {
		return this.vehicles.size();
	}

	public Renting getRenting(String reference) {
		for (Vehicle vehicle : this.vehicles) {
			Renting renting = vehicle.getRenting(reference);
			if(renting != null) {
				return renting;
			}
		}
		return null;
	}
	
	public static Set<Car> getAllAvailableCars(LocalDate begin, LocalDate end) {
		Set<Car> cars = new HashSet<>();
		for (RentACar rentacar : rentacars) {
			for (Vehicle vehicle : rentacar.vehicles) {
				if ((vehicle instanceof Car) && vehicle.isFree(begin, end)) {
					cars.add((Car) vehicle);
				}
			}
		}
		return cars;
	}

	public static Set<Motorcycle> getAllAvailableMotorcycles(LocalDate begin, LocalDate end) {
		Set<Motorcycle> motorcycles = new HashSet<>();
		for (RentACar rentacar : rentacars) {
			for (Vehicle vehicle : rentacar.vehicles) {
				if ((vehicle instanceof Motorcycle) && vehicle.isFree(begin, end)) {
					motorcycles.add((Motorcycle) vehicle);
				}
			}
		}
		return motorcycles;
	}

	public static RentingData getRentingData(String reference) {
		for (RentACar rentacar : rentacars) {
			for (Vehicle vehicle : rentacar.vehicles) {
				Renting renting = vehicle.getRenting(reference);
				if (renting != null) {
					return new RentingData(reference, vehicle.getPlate(), renting.getDrivingLicense(), rentacar.getCode(), renting.getBegin(), renting.getEnd());
				}
			}
		}
		throw new CarException();
	}

}
