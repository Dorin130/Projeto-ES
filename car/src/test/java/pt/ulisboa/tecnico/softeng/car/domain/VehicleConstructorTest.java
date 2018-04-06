package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class VehicleConstructorTest {
	private static final String PLATE_CAR = "22-33-HZ";
	private static final String PLATE_MOTORCYCLE = "44-33-HZ";
	private static final String RENT_A_CAR_NAME = "Eartz";
	private static final String NIF1 = "123456789";
	private static final String IBAN1 = "IBAN";
	private static final String NIF2 = "123456788";
	private static final String IBAN2 = "IBAN2";
	private static final int AMOUNT = 100;
	private RentACar rentACar;

	@Before
	public void setUp() {
		this.rentACar = new RentACar(RENT_A_CAR_NAME, NIF1, IBAN1);
	}

	@Test
	public void success() {
		Vehicle car = new Car(PLATE_CAR, 10, AMOUNT, this.rentACar);
		Vehicle motorcycle = new Motorcycle(PLATE_MOTORCYCLE, 10, AMOUNT, this.rentACar);

		assertEquals(PLATE_CAR, car.getPlate());
		assertTrue(this.rentACar.hasVehicle(PLATE_CAR));
		assertEquals(AMOUNT, car.getAmount());
		assertEquals(PLATE_MOTORCYCLE, motorcycle.getPlate());
		assertTrue(this.rentACar.hasVehicle(PLATE_MOTORCYCLE));
		assertEquals(AMOUNT, motorcycle.getAmount());
	}

	@Test(expected = CarException.class)
	public void emptyLicensePlate() {
		new Car("", 10, AMOUNT, this.rentACar);
	}

	@Test(expected = CarException.class)
	public void nullLicensePlate() {
		new Car(null, 10, AMOUNT, this.rentACar);
	}

	@Test(expected = CarException.class)
	public void invalidLicensePlate() {
		new Car("AA-XX-a", 10, AMOUNT, this.rentACar);
	}

	@Test(expected = CarException.class)
	public void invalidLicensePlate2() {
		new Car("AA-XX-aaa", 10, AMOUNT, this.rentACar);
	}

	@Test(expected = CarException.class)
	public void duplicatedPlate() {
		new Car(PLATE_CAR, 0, AMOUNT, this.rentACar);
		new Car(PLATE_CAR, 0, AMOUNT, this.rentACar);
	}

	@Test(expected = CarException.class)
	public void duplicatedPlateDifferentRentACar() {
		new Car(PLATE_CAR, 0, AMOUNT, rentACar);
		RentACar rentACar2 = new RentACar(RENT_A_CAR_NAME + "2", NIF2, IBAN2);
		new Car(PLATE_CAR, 2, AMOUNT, rentACar2);
	}

	@Test(expected = CarException.class)
	public void negativeKilometers() {
		new Car(PLATE_CAR, -1, AMOUNT, this.rentACar);
	}
	
	@Test(expected = CarException.class)
	public void negativeAmount() {
		new Car(PLATE_CAR, 10, -1, this.rentACar);
	}

	@Test(expected = CarException.class)
	public void noRentACar() {
		new Car(PLATE_CAR, 0, AMOUNT, null);
	}

	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
	}

}
