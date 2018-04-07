package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentingConstructorTest {
	private static final String RENT_A_CAR_NAME = "Eartz";
	private static final String PLATE_CAR = "22-33-HZ";
	private static final String DRIVING_LICENSE = "br112233";
	private static final LocalDate DATE1 = LocalDate.parse("2018-01-06");
	private static final LocalDate DATE2 = LocalDate.parse("2018-01-07");
	private static final String NIF1 = "123456789";
	private static final String IBAN1 = "IBAN";
	private static final String NIF2 = "123456778";
	private static final String IBAN2 = "IBAN2";
	private static final int AMOUNT = 100;
	private Car car;

	@Before
	public void setUp() {
		RentACar rentACar1 = new RentACar(RENT_A_CAR_NAME, NIF1, IBAN1);
		this.car = new Car(PLATE_CAR, 10, AMOUNT, rentACar1);
	}

	@Test
	public void success() {
		Renting renting = new Renting(DRIVING_LICENSE, DATE1, DATE2, car, NIF2, IBAN2);
		assertEquals(DRIVING_LICENSE, renting.getDrivingLicense());
		assertEquals(NIF2, renting.getBuyerNif());
		assertEquals(IBAN2, renting.getBuyerIban());
		assertEquals(DATE1, renting.getBegin());
		assertEquals(DATE2, renting.getEnd());
		assertEquals(null, renting.getCancelledPaymentReference());
		assertFalse(renting.isCancelledInvoice());
	}

	@Test(expected = CarException.class)
	public void nullDrivingLicense() {
		new Renting(null, DATE1, DATE2, car, NIF2, IBAN2);
	}

	@Test(expected = CarException.class)
	public void emptyDrivingLicense() {
		new Renting("", DATE1, DATE2, car, NIF2, IBAN2);
	}

	@Test(expected = CarException.class)
	public void invalidDrivingLicense() {
		new Renting("12", DATE1, DATE2, car, NIF2, IBAN2);
	}

	@Test(expected = CarException.class)
	public void nullBegin() {
		new Renting(DRIVING_LICENSE, null, DATE2, car, NIF2, IBAN2);
	}

	@Test(expected = CarException.class)
	public void nullEnd() {
		new Renting(DRIVING_LICENSE, DATE1, null, car, NIF2, IBAN2);
	}
	
	@Test(expected = CarException.class)
	public void endBeforeBegin() {
		new Renting(DRIVING_LICENSE, DATE2, DATE1, car, NIF2, IBAN2);
	}

	@Test(expected = CarException.class)
	public void nullCar() {
		new Renting(DRIVING_LICENSE, DATE1, DATE2, null, NIF2, IBAN2);
	}
	
	@Test(expected = CarException.class)
	public void nullNif() {
		new Renting(DRIVING_LICENSE, DATE1, DATE2, car, null, IBAN2);
	}
	
	@Test(expected = CarException.class)
	public void emptyNif() {
		new Renting(DRIVING_LICENSE, DATE1, DATE2, car, "", IBAN2);
	}
	
	@Test(expected = CarException.class)
	public void nullIban() {
		new Renting(DRIVING_LICENSE, DATE1, DATE2, car, NIF2, null);
	}
	
	@Test(expected = CarException.class)
	public void emptyIban() {
		new Renting(DRIVING_LICENSE, DATE1, DATE2, car, NIF2, "");
	}

	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
	}

}
