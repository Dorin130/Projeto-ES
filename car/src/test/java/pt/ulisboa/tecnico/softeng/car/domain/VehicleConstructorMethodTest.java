package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.VehicleException;
public class VehicleConstructorMethodTest{
	private String plate = "AA-09-BB";
	private int kilometers = 0;
	private RentACar rentACar;
	private ArrayList rentings =  new ArrayList();
	
	@Before
	public void setUp() {
		this.rentACar = new RentACar("um nome" ,"um codigo");
	}
	
	@Test
	public void success() {
		Car car = new Car(this.plate, this.kilometers , this.rentACar);
		Assert.assertEquals(this.plate, car.getPlate());
		Assert.assertEquals(this.kilometers, car.getKilometers());
		Assert.assertEquals(this.rentACar, car.getRentAcar())	;
	}
	
	@Test(expected = VehicleException.class)
	public void nullPlate() {
		Car car = new Car(null, this.kilometers , this.rentACar);
	}

	@Test(expected = VehicleException.class)
	public void nullRentACar() {
		Car car = new Car(this.plate, this.kilometers , null);
	}
	
	@Test(expected = VehicleException.class)
	public void badPlate() {
		Car car = new Car("AAAAA", this.kilometers , this.rentACar);
	}
	
	@Test(expected = VehicleException.class)
	public void badKilometers() {
		Car car = new Car(this.plate, -10, this.rentACar);
	}
	
}