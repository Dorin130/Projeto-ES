package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.car.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.car.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;

@RunWith(JMockit.class)
public class RentACarRentVehicleMethodTest {
	private static final String IBAN = "IBAN";
	private static final String NIF = "123456789";
	private static final String IBAN2 = "IBAN2";
	private static final String NIF2 = "123444789";
	private static final String IBAN3 = "IBAN3";
	private static final String NIF3 = "123440789";
	private static final String DRIVING_LICENSE = "br123";
	private static final String PLATE = "aa-00-11";
	private static final int KILOMETERS = 200;
	private static final int AMOUNT = 400;
	private static final LocalDate DATE1 = LocalDate.parse("2018-01-06");
	private static final LocalDate DATE2 = LocalDate.parse("2018-01-07");
	private static final LocalDate DATE3 = LocalDate.parse("2018-02-19");
	
	private static RentACar rentacar1;
	private static RentACar rentacar2;

	@Before
	public void setup() {
		rentacar1 = new RentACar("abc", NIF, IBAN);
		rentacar2 = new RentACar("def", NIF2, IBAN2);
	}

	@Test
	public void numberOfRentACars() {
		Assert.assertTrue(RentACar.rentACars.size() == 2);
	}

	@Test
	public void nameOfRentACars() {
		Assert.assertTrue(RentACar.rentACars.contains(rentacar1));
		Assert.assertTrue(RentACar.rentACars.contains(rentacar2));
	}

	@Test(expected = CarException.class)
	public void rentCarNoOption() {
		RentACar.rentCar(DRIVING_LICENSE, DATE3, DATE3, NIF3, IBAN3);
	}
	
	@Test(expected = CarException.class)
	public void rentMotorcycleNoOption() {
		RentACar.rentMotorcycle(DRIVING_LICENSE, DATE3, DATE3, NIF3, IBAN3);
	}

	@Test
	public void rentCar(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);

				TaxInterface.submitInvoice((InvoiceData) this.any);
			}
		};

		new Car(PLATE, KILOMETERS, AMOUNT, rentacar1);
		
		String car = RentACar.rentCar(DRIVING_LICENSE, DATE1, DATE2, NIF3, IBAN3);

		Assert.assertTrue(car != null);
	}
	
	@Test
	public void rentMotorcycle(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);

				TaxInterface.submitInvoice((InvoiceData) this.any);
			}
		};

		new Motorcycle(PLATE, KILOMETERS, AMOUNT, rentacar2);
		
		String motorcycle = RentACar.rentMotorcycle(DRIVING_LICENSE, DATE1, DATE2, NIF3, IBAN3);

		Assert.assertTrue(motorcycle != null);
	}

	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
	}
}
