package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class VehicleRentTest {
	private static final String PLATE_CAR = "22-33-HZ";
	private static final String RENT_A_CAR_NAME = "Eartz";
	private static final String DRIVING_LICENSE = "lx1423";
	private static final LocalDate DATE1 = LocalDate.parse("2018-01-06");
	private static final LocalDate DATE2 = LocalDate.parse("2018-01-09");
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

	@Test(expected = CarException.class)
	public void doubleRent() {
		car.rent(DRIVING_LICENSE, DATE1, DATE2, NIF2, IBAN2);
		car.rent(DRIVING_LICENSE, DATE1, DATE2, NIF2, IBAN2);
	}

	@Test(expected = CarException.class)
	public void beginIsNull() {
		RentACar rentACar = new RentACar(RENT_A_CAR_NAME, NIF1, IBAN1);
		Vehicle car = new Car(PLATE_CAR, 10, AMOUNT, rentACar);
		car.rent(DRIVING_LICENSE, null, DATE2, NIF2, IBAN2);
	}

	@Test(expected = CarException.class)
	public void endIsNull() {
		RentACar rentACar = new RentACar(RENT_A_CAR_NAME, NIF1, IBAN1);
		Vehicle car = new Car(PLATE_CAR, 10, AMOUNT, rentACar);
		car.rent(DRIVING_LICENSE, DATE1, null, NIF2, IBAN2);
	}

	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
	}
}
