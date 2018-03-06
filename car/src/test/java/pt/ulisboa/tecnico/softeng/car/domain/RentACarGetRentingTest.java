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
	private Vehicle vehicle;
	private Renting renting;
	
	@Before
	public void setUp() {
		this.rentacar = new RentACar("CompanyName");
		this.vehicle = new Vehicle("AA-01-BB", 100, this.rentacar);
		this.renting = this.vehicle.rent(DRIVING_LICENSE, this.begin, this.end);
	}
	
	@Test
	public void success() {
		assertEquals(this.renting, this.rentacar.getRenting(this.renting.getReference()));
	}
	
	@Test
	public void referenceDoesNotExist() {
		//TODO verify with teacher
		assertNull(this.rentacar.getRenting("123a"));
	}
	
	@Test
	public void nullReference() {
		//TODO verify with teacher
		assertNull(this.rentacar.getRenting(null));
	}
	
	public void emptyReference() {
		//TODO verify with teacher
		assertNull(this.rentacar.getRenting(""));
	}

	@After
	public void tearDown() {
		RentACar.rentacars.clear();
	}	
}
