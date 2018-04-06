package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.car.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;


@RunWith(JMockit.class)
public class RentACarCancelReservationMethodTest {
	private static final String IBAN = "IBAN";
	private static final String NIF = "123456789";
	private static final String IBAN2 = "IBAN2";
	private static final String NIF2 = "123452789";
	private static final String PLATE = "aa-00-11";
	private static final String PLATE2 = "aa-00-22";
	private static final String DRIVING_LICENSE = "br123";
	private static final int KILOMETERS = 200;
	private static final int AMOUNT = 200;
	private static final LocalDate DATE1 = LocalDate.parse("2018-01-06");
	private static final LocalDate DATE2 = LocalDate.parse("2018-01-07");
	private RentACar rentacar;
	private Car car;
	private Motorcycle motorcycle;

	@Before
	public void setUp() {
		this.rentacar = new RentACar("abc", NIF, IBAN);

		this.car = new Car(PLATE, KILOMETERS, AMOUNT, rentacar);
		this.motorcycle = new Motorcycle(PLATE2, KILOMETERS, AMOUNT, rentacar);
	}

	@Test
	public void success(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);

				TaxInterface.submitInvoice((InvoiceData) this.any);
			}
		};

		Renting renting = car.rent(DRIVING_LICENSE, DATE1, DATE2, NIF2, IBAN2);
		this.rentacar.getProcessor().submitRenting(renting);

		String cancel = RentACar.cancelReservation(renting.getReference());

		assertTrue(renting.isCancelled());
		assertEquals(cancel, renting.getCancel());
	}

	@Test(expected = CarException.class)
	public void doesNotExist(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);

				TaxInterface.submitInvoice((InvoiceData) this.any);
			}
		};

		this.rentacar.getProcessor().submitRenting(motorcycle.rent(DRIVING_LICENSE, DATE1, DATE2, NIF2, IBAN2));

		RentACar.cancelReservation("XPTO");
	}

	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
	}

}

