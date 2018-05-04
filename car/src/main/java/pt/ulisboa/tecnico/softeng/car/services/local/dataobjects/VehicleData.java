package pt.ulisboa.tecnico.softeng.car.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.car.domain.Vehicle;


public class VehicleData {
    private String plate;
    private int kilometers;
    private double price;
    private String type;
    
    public VehicleData() {}
  
	public VehicleData(Vehicle vehicle, String type) {
		this.plate = vehicle.getPlate();
		this.kilometers = vehicle.getKilometers();
		this.price = vehicle.getPrice();
		this.type = type;
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

}
