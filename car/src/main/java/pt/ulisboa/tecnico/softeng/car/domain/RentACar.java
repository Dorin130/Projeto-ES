package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACar {
	public static final Set<RentACar> rentACars = new HashSet<>();

	private static int counter;

	private final String name;
	private final String code;
	private final String nif;
	private final String iban;
	
	private final Map<String, Vehicle> vehicles = new HashMap<>();

	private final Processor processor = new Processor();
	
	public RentACar(String name, String nif, String iban) {
		checkArguments(name, nif, iban);
		this.name = name;
		this.nif = nif;
		this.iban = iban;
		this.code = Integer.toString(++RentACar.counter);

		rentACars.add(this);
	}

	private void checkArguments(String name, String nif, String iban) {
		if (name == null || name.isEmpty() || nif == null || nif.trim().length() == 0 || iban == null || iban.trim().length() == 0) {
			throw new CarException();
		}
		
		for (RentACar rentacar : rentACars) {
			if (rentacar.getNif().equals(nif)) {
				throw new CarException();
			}
		}
		
		for (RentACar rentacar : rentACars) {
			if (rentacar.getIban().equals(iban)) {
				throw new CarException();
			}
		}
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	public String getNif() {
		return this.nif;
	}
	
	public String getIban() {
		return this.iban;
	}
	
	public Processor getProcessor() {
		return this.processor;
	}

	void addVehicle(Vehicle vehicle) {
		this.vehicles.put(vehicle.getPlate(), vehicle);
	}

	public boolean hasVehicle(String plate) {
		return vehicles.containsKey(plate);
	}

	public Set<Vehicle> getAvailableVehicles(Class<?> cls, LocalDate begin, LocalDate end) {
		Set<Vehicle> availableVehicles = new HashSet<>();
		for (Vehicle vehicle : this.vehicles.values()) {
			if (cls == vehicle.getClass() && vehicle.isFree(begin, end)) {
				availableVehicles.add(vehicle);
			}
		}
		return availableVehicles;
	}

	private static Set<Vehicle> getAllAvailableVehicles(Class<?> cls, LocalDate begin, LocalDate end) {
		Set<Vehicle> vehicles = new HashSet<>();
		for (RentACar rentACar : rentACars) {
			vehicles.addAll(rentACar.getAvailableVehicles(cls, begin, end));
		}
		return vehicles;
	}

	public static Set<Vehicle> getAllAvailableMotorcycles(LocalDate begin, LocalDate end) {
		return getAllAvailableVehicles(Motorcycle.class, begin, end);
	}

	public static Set<Vehicle> getAllAvailableCars(LocalDate begin, LocalDate end) {
		return getAllAvailableVehicles(Car.class, begin, end);
	}

	/**
	 * Lookup for a renting using its reference.
	 * 
	 * @param reference
	 * @return the renting with the given reference.
	 */
	public static Renting getRenting(String reference) {
		for (RentACar rentACar : rentACars) {
			for (Vehicle vehicle : rentACar.vehicles.values()) {
				Renting renting = vehicle.getRenting(reference);
				if (renting != null) {
					return renting;
				}
			}
		}
		return null;
	}

	public static RentingData getRentingData(String reference) {
		Renting renting = getRenting(reference);
		if (renting == null) {
			throw new CarException();
		}
		return new RentingData(renting);
	}
	
	public static String rentVehicle(Class<?> cls, String drivingLicense, LocalDate begin, LocalDate end, String nif, String iban) {
		Set<Vehicle> vehicles = getAllAvailableVehicles(cls, begin, end);;
		
		if (!vehicles.isEmpty()) {
			Vehicle vehicleToRent = vehicles.iterator().next();
			RentACar rentacar = vehicleToRent.getRentACar();
			
			Renting renting = vehicleToRent.rent(drivingLicense, begin, end, nif, iban);
			rentacar.getProcessor().submitRenting(renting);
			return renting.getReference();
		}
		throw new CarException();
	}
	
	public static String rentCar(String drivingLicense, LocalDate begin, LocalDate end, String nif, String iban) {
		return rentVehicle(Car.class, drivingLicense, begin, end, nif ,iban);
	}

	public static String rentMotorcycle(String drivingLicense, LocalDate begin, LocalDate end, String nif, String iban) {
		return rentVehicle(Motorcycle.class, drivingLicense, begin, end, nif ,iban);
	}

	public static String cancelRenting(String reference) {
		Renting renting = getRenting(reference);
		if (renting != null) {
			return renting.cancel();
		}
		throw new CarException();
	}

	/*
	public static String processRenting() {
	//TODO
		return "";
	} */
}
