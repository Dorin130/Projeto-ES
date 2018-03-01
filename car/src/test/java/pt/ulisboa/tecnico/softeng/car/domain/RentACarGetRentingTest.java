package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACarGetRentingTest {
	private static final String DRIVING_LICENSE = "123456";
	private final LocalDate begin = new LocalDate(2018, 01, 01);
	private final LocalDate end = new LocalDate(2018, 01, 03);
	private RentACar rentacar;
	private Renting renting;
	
	@Before
	public void setUp() {
		this.rentacar = new RentACar("CompanyName");
		this.renting = this.rentacar.rent(this.rentacar, DRIVING_LICENSE, this.begin, this.end);
	}
	
	@Test
	public void sucess() {
		Assert.assertEquals(this.renting, this.rentacar.getRenting(this.renting.getReference()));
	}
	
	@Test
	public void successCancelled() {
		this.renting.cancel();
		Assert.assertEquals(this.renting, this.rentacar.getRenting(this.renting.getCancellation()));
	}
	
	@Test
	public void doesNotExist() {
		Assert.assertNull(this.rentacar.getRenting("123a"));
	}

	@After
	public void tearDown() {
		RentACar.rentacars.clear();
	}
	
}
