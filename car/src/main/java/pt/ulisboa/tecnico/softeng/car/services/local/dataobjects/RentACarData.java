package pt.ulisboa.tecnico.softeng.car.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.car.domain.Car;
import pt.ulisboa.tecnico.softeng.car.domain.RentACar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RentACarData {
	private String code;
	private String name;
	private String nif;
	private String iban;
	private List<VehicleData> vehicles = new ArrayList<>();

	public RentACarData() {
	}

	public RentACarData(RentACar rentacar) {
		this.code = rentacar.getCode();
		this.name = rentacar.getName();
		this.nif = rentacar.getNif();
		this.iban = rentacar.getIban();
		
		this.vehicles = rentacar.getVehicleSet().stream().map(v -> {
			if(v instanceof Car) { return new VehicleData(v, "Car");} 
			else {return new VehicleData(v, "Motorcycle"); }}
		).collect(Collectors.toList());

	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNif() {
		return this.nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}
	
	public List<VehicleData> getVehicleSet() {
		return vehicles;
	}
	
}
