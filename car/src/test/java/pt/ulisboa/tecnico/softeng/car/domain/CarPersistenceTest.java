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
	private static final String ACTIVITY_NAME = "Activity_Name";
	private static final String RENT_A_CAR_NAME = "rent_a_car";
	private static final String PROVIDER_NAME = "Wicket";
	private static final String PROVIDER_CODE = "A12345";
	private static final String IBAN = "IBAN";
	private static final String NIF = "NIF";
	private static final String BUYER_IBAN = "IBAN2";
	private static final String BUYER_NIF = "NIF2";
	private static final int CAPACITY = 25;
	private String code;
	private final LocalDate begin = new LocalDate(2017, 04, 01);
	private final LocalDate end = new LocalDate(2017, 04, 15);

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		RentACar rentACar = new RentACar(RENT_A_CAR_NAME , NIF, IBAN);
		this.code = rentACar.getCode();
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		RentACar rentACar = RentACar.findRentACarByCode(this.code);
		Assert.assertEquals(IBAN, rentACar.getIban());
		Assert.assertEquals(NIF, rentACar.getNif());
		Assert.assertEquals(RENT_A_CAR_NAME, rentACar.getName());

	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for( RentACar rentACar : RentACar.getRentACars()) {
			rentACar.delete();
		}
	}

}
