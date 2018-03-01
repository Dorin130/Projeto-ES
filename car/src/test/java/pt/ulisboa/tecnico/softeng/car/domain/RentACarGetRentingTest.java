package pt.ulisboa.tecnico.softeng.car.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACarGetRentingTest {
	private static final String RENTING_REFERENCE = "ABC123";
	private RentACar rentacar;
	private Vehicle vehicle;
	private Renting renting;
	
	@Before
	public void setUp() {
		this.rentacar = new RentACar("CompanyName");
		this.vehicle = new Vehicle("AA-09-BB", 0, rentacar);
		this.renting = new Renting(RENTING_REFERENCE, vehicle);
	}
	
	@Test
	public void sucess() {
		Renting result = this.rentacar.getRenting(this.renting.getReference());
	
		Assert.assertEquals(RENTING_REFERENCE, this.renting.getReference());
		Assert.assertEquals(this.renting, result);
	}
	
	
}
