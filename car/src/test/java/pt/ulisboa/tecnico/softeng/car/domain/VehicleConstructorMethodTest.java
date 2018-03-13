package pt.ulisboa.tecnico.softeng.car.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;
public class VehicleConstructorMethodTest{
	private String plate = "AA-09-BB";
	private int kilometers = 0;
	private RentACar rentACar;
	
	@Before
	public void setUp() {
		this.rentACar = new RentACar("um nome");
	}
	
	@Test
	public void success() {
		Car car = new Car(this.plate, this.kilometers , this.rentACar);
		Assert.assertEquals(this.plate, car.getPlate());
		Assert.assertEquals(this.kilometers, car.getKilometers());
		Assert.assertEquals(this.rentACar, car.getRentAcar());
	}
	
	@Test(expected = CarException.class)
	public void nullPlate() {
		new Car(null, this.kilometers , this.rentACar);
	}

	@Test(expected = CarException.class)
	public void nullRentACar() {
		new Car(this.plate, this.kilometers , null);
	}
	
	@Test(expected = CarException.class)
	public void badPlate() {
		new Car("AAAAA", this.kilometers , this.rentACar);
	}
	
	@Test(expected = CarException.class)
	public void badKilometers() {
		new Car(this.plate, -10, this.rentACar);
	}
	
	@Test(expected = CarException.class)
	public void addNegativeKm() {
		Car car = new Car(this.plate, this.kilometers , this.rentACar);
		car.addKilometers(-10);
	}
	
	@After
	public void tearDown() {
		RentACar.rentacars.clear();
	}
	
}