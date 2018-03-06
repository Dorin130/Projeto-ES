package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;

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
	public void success() {
		assertEquals(this.renting, this.rentacar.getRenting(this.renting.getReference()));
	}
	
	@Test
	public void successCancelled() {
		this.renting.cancel();
		assertEquals(this.renting, this.rentacar.getRenting(this.renting.getCancellation()));
	}
	
	@Test
	public void doesNotExist() {
		assertNull(this.rentacar.getRenting("123a"));
	}

	@After
	public void tearDown() {
		RentACar.rentacars.clear();
	}	
}
