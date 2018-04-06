package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class VehicleIsFreeTest {

	private static final String PLATE_CAR = "22-33-HZ";
	private static final String RENT_A_CAR_NAME = "Eartz";
	private static final String DRIVING_LICENSE = "lx1423";
	private static final LocalDate DATE1 = LocalDate.parse("2018-01-06");
	private static final LocalDate DATE2 = LocalDate.parse("2018-01-07");
	private static final LocalDate DATE3 = LocalDate.parse("2018-01-08");
	private static final LocalDate DATE4 = LocalDate.parse("2018-01-09");
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

	@Test
	public void noBookingWasMade() {
		assertTrue(car.isFree(DATE1, DATE2));
		assertTrue(car.isFree(DATE1, DATE3));
		assertTrue(car.isFree(DATE3, DATE4));
		assertTrue(car.isFree(DATE4, DATE4));
	}

	@Test
	public void bookingsWereMade() {
		car.rent(DRIVING_LICENSE, DATE2, DATE2, NIF2, IBAN2);
		car.rent(DRIVING_LICENSE, DATE3, DATE4, NIF2, IBAN2);

		assertFalse(car.isFree(DATE1, DATE2));
		assertFalse(car.isFree(DATE1, DATE3));
		assertFalse(car.isFree(DATE3, DATE4));
		assertFalse(car.isFree(DATE4, DATE4));
		assertTrue(car.isFree(DATE1, DATE1));
	}
	
	@Test(expected = CarException.class)
	public void nullBegin() {
		car.rent(DRIVING_LICENSE, null, DATE2, NIF2, IBAN2);
	}
	
	@Test(expected = CarException.class)
	public void nullEnd() {
		car.rent(DRIVING_LICENSE, DATE1, null, NIF2, IBAN2);
	}

	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
	}
}
