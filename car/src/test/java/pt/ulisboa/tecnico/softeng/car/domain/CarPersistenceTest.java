package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class CarPersistenceTest {
	private static final String PLATE_CAR = "22-33-HZ";
	private static final String PLATE_MOTORCYCLE = "44-33-HZ";
	private static final String RENT_A_CAR_NAME = "rent_a_car";
	private static final String IBAN = "IBAN";
	private static final String NIF = "NIF";

	private static final LocalDate date1 = LocalDate.parse("2018-01-06");
	private static final LocalDate date2 = LocalDate.parse("2018-01-09");

	private String code;

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		RentACar rentACar = new RentACar(RENT_A_CAR_NAME , NIF, IBAN);
		new Car(PLATE_CAR,10, 10, rentACar);
		new Motorcycle(PLATE_MOTORCYCLE,20, 5, rentACar);

		this.code = rentACar.getCode();
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		RentACar rentACar = RentACar.findRentACarByCode(this.code);
		Assert.assertEquals(IBAN, rentACar.getIban());
		Assert.assertEquals(NIF, rentACar.getNif());
		Assert.assertEquals(RENT_A_CAR_NAME, rentACar.getName());

		Processor proc = rentACar.getProcessor();
		
		assertNotNull(proc);
				
		List<Vehicle> cars = new ArrayList<>(RentACar.getAllAvailableCars(date1, date2));
		Assert.assertEquals(1, cars.size());
		Vehicle car = cars.get(0);
		Assert.assertEquals(PLATE_CAR, car.getPlate());
		Assert.assertEquals(10, car.getKilometers());
		Assert.assertEquals(10, car.getPrice(), 0);

		List<Vehicle> motorcycles = new ArrayList<>(RentACar.getAllAvailableMotorcycles(date1, date2));
		Assert.assertEquals(1, motorcycles.size());
		Vehicle motorcycle = motorcycles.get(0);
		Assert.assertEquals(PLATE_MOTORCYCLE, motorcycle.getPlate());
		Assert.assertEquals(20, motorcycle.getKilometers());
		Assert.assertEquals(5, motorcycle.getPrice(), 0);

	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for( RentACar rentACar : RentACar.getRentACars()) {
			rentACar.delete();
		}
	}

}
