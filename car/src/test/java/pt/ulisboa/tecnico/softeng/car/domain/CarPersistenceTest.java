package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class CarPersistenceTest {
	private static final String PLATE_CAR = "22-33-HZ";
    private static final String PLATE_CAR2 = "23-32-AZ";
	private static final String PLATE_MOTORCYCLE = "44-33-HZ";
	private static final String RENT_A_CAR_NAME = "rent_a_car";
	private static final String IBAN = "IBAN";
	private static final String NIF = "NIF";
	private static final String DRIVING_LICENSE = "lx1423";
	private static final String NIF_BUYER = "NIF2";
	private static final String IBAN_BUYER = "IBAN2";
	private static final String TESTSTRING = "PERSISTENCETEST";

	private static final LocalDate date1 = LocalDate.parse("2018-01-06");
	private static final LocalDate date2 = LocalDate.parse("2018-01-09");

	private String code;
	private Car car;

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		RentACar rentACar = new RentACar(RENT_A_CAR_NAME , NIF, IBAN);
		new Car(PLATE_CAR,10, 10, rentACar);
        this.car = new Car(PLATE_CAR2,10, 10, rentACar);
		new Motorcycle(PLATE_MOTORCYCLE,20, 5, rentACar);

        Renting renting = this.car.rent(DRIVING_LICENSE, date1, date2, NIF_BUYER, IBAN_BUYER);
        /* Using setters to test persistence */
		renting.setCancellationReference(TESTSTRING);
		renting.setPaymentReference(TESTSTRING);
		renting.setCancelledPaymentReference(TESTSTRING);
		renting.setCancellationDate(date1);
		renting.setInvoiceReference(TESTSTRING);

		this.code = rentACar.getCode();
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		RentACar rentACar = RentACar.findRentACarByCode(this.code);
		Assert.assertEquals(IBAN, rentACar.getIban());
		Assert.assertEquals(NIF, rentACar.getNif());
		Assert.assertEquals(RENT_A_CAR_NAME, rentACar.getName());

		Processor proc = rentACar.getProcessor();
		Assert.assertNotNull(proc);
				
		List<Vehicle> cars = new ArrayList<>(RentACar.getAllAvailableCars(date1, date2));
		Assert.assertEquals(1, cars.size());
		Vehicle car = cars.get(0);
		Assert.assertEquals(PLATE_CAR, car.getPlate());
		Assert.assertEquals(10, car.getKilometers());
		Assert.assertEquals(10, car.getPrice(), 0);
		Assert.assertNotNull(car.getRentACar());

		List<Vehicle> motorcycles = new ArrayList<>(RentACar.getAllAvailableMotorcycles(date1, date2));
		Assert.assertEquals(1, motorcycles.size());
		Vehicle motorcycle = motorcycles.get(0);
		Assert.assertEquals(PLATE_MOTORCYCLE, motorcycle.getPlate());
		Assert.assertEquals(20, motorcycle.getKilometers());
		Assert.assertEquals(5, motorcycle.getPrice(), 0);
		Assert.assertNotNull(motorcycle.getRentACar());

		List<Renting> rentings = new ArrayList<>(this.car.getRentingSet());
		Assert.assertEquals(1, rentings.size());
		Renting renting = rentings.get(0);
		Assert.assertEquals(DRIVING_LICENSE , renting.getDrivingLicense());
		Assert.assertEquals(date1 , renting.getBegin());
		Assert.assertEquals(date2 , renting.getEnd());
		Assert.assertEquals(NIF_BUYER , renting.getClientNIF());
		Assert.assertEquals(IBAN_BUYER , renting.getClientIBAN());
		Assert.assertEquals(false, renting.getCancelledInvoice());
		Assert.assertNotNull(renting.getVehicle());
		Assert.assertNotNull(renting.getKilometers());
		Assert.assertNotNull(renting.getReference());
		Assert.assertNotNull(renting.getPrice());

		Assert.assertEquals(TESTSTRING , renting.getCancellationReference());
		Assert.assertEquals(TESTSTRING , renting.getPaymentReference());
		Assert.assertEquals(TESTSTRING , renting.getCancelledPaymentReference());
		Assert.assertEquals(TESTSTRING , renting.getInvoiceReference());
		Assert.assertEquals(date1 , renting.getCancellationDate());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for( RentACar rentACar : RentACar.getRentACars()) {
			rentACar.delete();
		}
	}

}
