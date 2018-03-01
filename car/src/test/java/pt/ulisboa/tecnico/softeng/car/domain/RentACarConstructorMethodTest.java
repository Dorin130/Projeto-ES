package pt.ulisboa.tecnico.softeng.car.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACarConstructorMethodTest {
	private static final String RENTACAR_NAME = "CompanyName";
	
    @Test
    public void success() {
		RentACar rentacar = new RentACar(RENTACAR_NAME);

		Assert.assertEquals(RENTACAR_NAME, rentacar.getName());
		Assert.assertEquals(1, RentACar.rentacars.size());
	}

	@Test(expected = CarException.class)
	public void nullName() {
		new RentACar(null);
	}

	@Test(expected = CarException.class)
	public void emptyName() {
		new RentACar("");
	}
	
	@After
	public void tearDown() {
		RentACar.rentacars.clear();
	}

}
