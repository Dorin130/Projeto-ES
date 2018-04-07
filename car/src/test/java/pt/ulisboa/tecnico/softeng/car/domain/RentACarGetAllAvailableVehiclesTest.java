package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RentACarGetAllAvailableVehiclesTest {

	private static final String NAME1 = "eartz";
	private static final String NAME2 = "eartz";
	private static final String PLATE_CAR1 = "aa-00-11";
	private static final String PLATE_CAR2 = "aa-00-22";
	private static final String PLATE_MOTORCYCLE = "44-33-HZ";
	private static final String DRIVING_LICENSE = "br123";
	private static final LocalDate DATE1 = LocalDate.parse("2018-01-06");
	private static final LocalDate DATE2 = LocalDate.parse("2018-01-07");
	private static final LocalDate DATE3 = LocalDate.parse("2018-01-08");
	private static final LocalDate DATE4 = LocalDate.parse("2018-01-09");
	private static final String NIF1 = "123456789";
	private static final String IBAN1 = "IBAN";
	private static final String NIF2 = "123456788";
	private static final String IBAN2 = "IBAN2";
	private static final int AMOUNT = 100;
	private static final String NIF3 = "123456778";
	private static final String IBAN3 = "IBAN3";
	private RentACar rentACar1;
	private RentACar rentACar2;

	@Before
	public void setUp() {
		this.rentACar1 = new RentACar(NAME1, NIF1, IBAN1);
		this.rentACar2 = new RentACar(NAME2, NIF2, IBAN2);
	}

	@Test
	public void onlyCars() {
		Vehicle car1 = new Car(PLATE_CAR1, 10, AMOUNT, rentACar1);
		car1.rent(DRIVING_LICENSE, DATE1, DATE2, NIF3, IBAN3);
		Vehicle car2 = new Car(PLATE_CAR2, 10, AMOUNT, rentACar2);
		Vehicle motorcycle = new Motorcycle(PLATE_MOTORCYCLE, 10, AMOUNT, rentACar1);

		Set<Vehicle> cars = RentACar.getAllAvailableCars(DATE3, DATE4);
		assertTrue(cars.contains(car1));
		assertTrue(cars.contains(car2));
		assertFalse(cars.contains(motorcycle));
	}

	@Test
	public void onlyAvailableCars() {
		Vehicle car1 = new Car(PLATE_CAR1, 10, AMOUNT, rentACar1);
		Vehicle car2 = new Car(PLATE_CAR2, 10, AMOUNT, rentACar2);

		car1.rent(DRIVING_LICENSE, DATE1, DATE2, NIF3, IBAN3);
		Set<Vehicle> cars = RentACar.getAllAvailableCars(DATE1, DATE2);

		assertFalse(cars.contains(car1));
		assertTrue(cars.contains(car2));
	}
	
	@Test
	public void onlyMotorcycles() {
		Vehicle car = new Car(PLATE_CAR1, 10, AMOUNT, rentACar1);
		Vehicle motorcycle = new Motorcycle(PLATE_MOTORCYCLE, 10, AMOUNT, rentACar1);

		Set<Vehicle> cars = RentACar.getAllAvailableMotorcycles(DATE3, DATE4);
		assertTrue(cars.contains(motorcycle));
		assertFalse(cars.contains(car));
	}

	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
	}
}
