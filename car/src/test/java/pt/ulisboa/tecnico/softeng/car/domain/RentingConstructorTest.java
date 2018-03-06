package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentingConstructorTest {
	private static final String DRIVER_LICENSE = "C777222";
	private final LocalDate begin = new LocalDate(2018, 12, 15);
	private final LocalDate end = new LocalDate(2018, 12, 20);
	private RentACar rentacar;
	
	@Before
	public void setUp() {
		this.rentacar = new RentACar("CompanyName");
	}
	
	@Test
	public void success() {
		Renting renting = new Renting(this.rentacar, DRIVER_LICENSE, this.begin, this.end);

		assertTrue(renting.getReference().startsWith(this.rentacar.getCode()));
		//assertTrue(renting.getReference().length() > RentACar.CODE_SIZE);
		assertEquals(this.begin, renting.getBegin());
		assertEquals(this.end, renting.getEnd());
	}
	
	@Test(expected = CarException.class)
	public void nullRentACar() {
		new Renting(null, DRIVER_LICENSE, this.begin, this.end);
	}
	
	@Test(expected = CarException.class)
	public void nullDriverLicense() {
		new Renting(null, null, this.begin, this.end);
	}

	@Test(expected = CarException.class)
	public void nullBegin() {
		new Renting(this.rentacar, DRIVER_LICENSE, null, this.end);
	}

	@Test(expected = CarException.class)
	public void nullEnd() {
		new Renting(this.rentacar, DRIVER_LICENSE, this.begin, null);
	}

	//TODO driver license tests - see parameterized tests
	//https://github.com/junit-team/junit4/wiki/parameterized-tests
	
	@Test(expected = CarException.class)
	public void endBeforeBegin() {
		new Renting(null, DRIVER_LICENSE, this.begin, this.begin.minusDays(1));
	}

	@Test
	public void arrivalEqualDeparture() {
		new Renting(this.rentacar, DRIVER_LICENSE, this.begin, this.begin);
	}


	
	@After
	public void tearDown() {
		RentACar.rentacars.clear();
	}
	
}
