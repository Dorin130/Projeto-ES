package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentingCheckoutMethodTest {
	private final LocalDate begin = new LocalDate(2018, 01, 01);
	private final LocalDate end = new LocalDate(2018, 01, 03);
	private RentACar rentacar;
	private Car car;
	private Renting renting;
	
	@Before
	public void setUp() {
		rentacar = new RentACar("CompanyName");
		car = new Car("AA-AA-AA", 0, rentacar);
		renting = new Renting(this.car, "C123456", this.begin, this.end);
	}
	
	@Test
	public void sucess() {
		renting.checkout(50);
		
		assertEquals(50, renting.getKilometers());
		assertEquals(50, car.getKilometers());
		assertTrue(renting.isCheckedOut()); //check specification with teacher
		assertFalse(renting.conflict(begin, end)); //check specification with teacher
	}
	
	@Test
	public void sucessWhenValueZero() {
		renting.checkout(0);
		
		assertEquals(0, renting.getKilometers());
		assertEquals(0, car.getKilometers());
		assertTrue(renting.isCheckedOut()); //check specification with teacher
		assertFalse(renting.conflict(begin, end)); //check specification with teacher
	}
	
	@Test(expected = CarException.class)
	public void failureBecauseNegativeValue() {
		this.renting.checkout(-1);
		assertEquals(0, car.getKilometers());
	}
	
	@Test(expected = CarException.class)
	public void failureOnDoubleCheckout() {
		this.renting.checkout(0);
		this.renting.checkout(50);
	}
	
	@Test
	public void doubleCheckoutDoesntRuinConflict() {
		this.renting.checkout(50);
		try {
			this.renting.checkout(50);
			fail();
		} catch (CarException c) {
			assertTrue(renting.isCheckedOut());
			assertEquals(50, car.getKilometers());
			assertFalse(renting.conflict(begin, end));
		}
	}

	@After
	public void tearDown() {
		RentACar.rentacars.clear();
	}
	
}
