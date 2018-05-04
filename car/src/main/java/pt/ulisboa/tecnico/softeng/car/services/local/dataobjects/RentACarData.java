package pt.ulisboa.tecnico.softeng.car.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.car.domain.Car;
import pt.ulisboa.tecnico.softeng.car.domain.RentACar;
import pt.ulisboa.tecnico.softeng.car.domain.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class RentACarData {
	private String code;
	private String name;
	private String nif;
	private String iban;
	private List<VehicleData> vehicles = new ArrayList<>();
	//TODO: FIX THIS

	public RentACarData() {
	}

	public RentACarData(RentACar rentacar) {
		this.code = rentacar.getCode();
		this.name = rentacar.getName();
		this.nif = rentacar.getNif();
		this.iban = rentacar.getIban();
		
		for(Vehicle vehicle : rentacar.getVehicleSet()) {
			if(vehicle instanceof Car ) {
				this.vehicles.add(new VehicleData(vehicle, "Car"));
			}else{
				this.vehicles.add(new VehicleData(vehicle, "Motorcycle"));
			}
			
		}
		
		/*this.setActivities(provider.getActivitySet().stream().sorted((a1, a2) -> a1.getName().compareTo(a2.getName()))
				.map(a -> new ActivityData(a)).collect(Collectors.toList()));*/
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
/*
	public List<ActivityData> getActivities() {
		return this.activities;
	}

	public void setActivities(List<ActivityData> activities) {
		this.activities = activities;
	}
*/
}
