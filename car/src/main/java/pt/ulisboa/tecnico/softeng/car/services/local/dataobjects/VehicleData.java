package pt.ulisboa.tecnico.softeng.car.services.local.dataobjects;

import java.util.List;
import java.util.stream.Collectors;

import pt.ulisboa.tecnico.softeng.car.domain.Vehicle;


public class VehicleData {
    private String plate;
    private int kilometers;
    private double price;
    private String type;
    private String rentacarName;
    private String rentacarCode;
    private List<RentingData> rentings;
    
    public VehicleData() {
    }
  
	public VehicleData(Vehicle vehicle, String type) {
		this.plate = vehicle.getPlate();
		this.kilometers = vehicle.getKilometers();
		this.price = vehicle.getPrice();
		this.type = type;
		this.rentacarName = vehicle.getRentACar().getName();
		this.rentacarCode = vehicle.getRentACar().getCode();
		
		this.rentings = vehicle.getRentingSet().stream().sorted((r1, r2) -> r1.getBegin().compareTo(r2.getBegin()))
				.map(r -> new RentingData(r)).collect(Collectors.toList());
	}

    public String getPlate() {
		return plate;
	}
	public void setPlate(String plate) {
		this.plate = plate;
	}
	public int getKilometers() {
		return kilometers;
	}
	public void setKilometers(int kilometers) {
		this.kilometers = kilometers;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	 public String getType() {
			return type;
		}
	public void setType(String type) {
		this.type = type;
	}

	public String getRentacarName() {
		return this.rentacarName;
	}

	public void setRentacarName(String rentacarName) {
		this.rentacarName = rentacarName;
	}

	public String getRentacarCode() {
		return this.rentacarCode;
	}

	public void setRentacarCode(String rentacarCode) {
		this.rentacarCode = rentacarCode;
	}

	public List<RentingData> getRentings() {
		return this.rentings;
	}
	
	public void setRentings(List<RentingData> rentings) {
		this.rentings = rentings;
	}
}
