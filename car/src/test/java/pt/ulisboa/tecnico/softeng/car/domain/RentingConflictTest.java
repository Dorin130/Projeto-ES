package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentingConflictTest {
	private static final String PLATE_CAR = "22-33-HZ";
	private static final String DRIVING_LICENSE = "br112233";
	private static final LocalDate DATE0 = LocalDate.parse("2018-01-05");
	private static final LocalDate DATE1 = LocalDate.parse("2018-01-06");
	private static final LocalDate DATE2 = LocalDate.parse("2018-01-07");
	private static final LocalDate DATE3 = LocalDate.parse("2018-01-08");
	private static final LocalDate DATE4 = LocalDate.parse("2018-01-09");
	private static final String RENT_A_CAR_NAME = "Eartz";
	private static final String NIF1 = "123456789";
	private static final String IBAN1 = "IBAN";
	private static final String NIF2 = "123456778";
	private static final String IBAN2 = "IBAN2";
	private static final int AMOUNT = 100;
	private Car car;

	@Before
	public void setUp() {
		RentACar rentACar = new RentACar(RENT_A_CAR_NAME, NIF1, IBAN1);
		this.car = new Car(PLATE_CAR, 10, AMOUNT, rentACar);
	}

	@Test()
	public void retingIsBeforeDates() {
		Renting renting = new Renting(DRIVING_LICENSE, DATE1, DATE2, car, NIF2, IBAN2);
		assertFalse(renting.conflict(DATE3, DATE4));
	}

	@Test()
	public void retingIsBeforeDatesSameDayInterval() {
		Renting renting = new Renting(DRIVING_LICENSE, DATE1, DATE2, car, NIF2, IBAN2);
		assertFalse(renting.conflict(DATE3, DATE3));
	}

	@Test()
	public void rentingEndsOnStartDate() {
		Renting renting = new Renting(DRIVING_LICENSE, DATE1, DATE2, car, NIF2, IBAN2);
		assertTrue(renting.conflict(DATE2, DATE3));
	}

	@Test()
	public void rentingStartsOnEndDate() {
		Renting renting = new Renting(DRIVING_LICENSE, DATE1, DATE2, car, NIF2, IBAN2);
		assertTrue(renting.conflict(DATE1, DATE1));
	}

	@Test()
	public void rentingStartsDuringInterval() {
		Renting renting = new Renting(DRIVING_LICENSE, DATE1, DATE2, car, NIF2, IBAN2);
		assertTrue(renting.conflict(DATE0, DATE3));
	}

	@Test(expected = CarException.class)
	public void endBeforeBegin() {
		Renting renting = new Renting(DRIVING_LICENSE, DATE1, DATE2, car, NIF2, IBAN2);
		renting.conflict(DATE2, DATE1);
	}

	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
	}
}
