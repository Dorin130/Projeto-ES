package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACarConstructorTest {
	private static final String NAME = "eartz";
	private static final String NAME2 = "eartz2";
	private static final String NIF = "123456789";
	private static final String NIF2 = "103456789";
	private static final String IBAN = "IBAN";
	private static final String IBAN2 = "IBAN2";

	@Test
	public void success() {
		RentACar rentACar = new RentACar(NAME, NIF, IBAN);
		assertEquals(NAME, rentACar.getName());
		assertEquals(NIF, rentACar.getNif());
		assertEquals(IBAN, rentACar.getIban());
	}

	@Test(expected = CarException.class)
	public void nullName() {
		new RentACar(null, NIF, IBAN);
	}

	@Test(expected = CarException.class)
	public void emptyName() {
		new RentACar("", NIF, IBAN);
	}
	
	@Test(expected = CarException.class)
	public void nullNif() {
		new RentACar(NAME, null, IBAN);
	}

	@Test(expected = CarException.class)
	public void emptyNif() {
		new RentACar(NAME, "", IBAN);
	}
	
	@Test(expected = CarException.class)
	public void nullIban() {
		new RentACar(NAME, NIF, null);
	}

	@Test(expected = CarException.class)
	public void emptyIban() {
		new RentACar(NAME, NIF, "");
	}
	
	@Test(expected = CarException.class)
	public void duplicatedNif() {
		new RentACar(NAME, NIF, IBAN);
		new RentACar(NAME2, NIF, IBAN2);
	}
	
	@Test(expected = CarException.class)
	public void duplicatedIban() {
		new RentACar(NAME, NIF, IBAN);
		new RentACar(NAME2, NIF2, IBAN);
	}
	
	/*
	@Test(expected = CarException.class)
	public void nullNameAndNif() {
		new RentACar(null, null, IBAN, VEHICLES_PRICE);
	}

	@Test(expected = CarException.class)
	public void nullNameAndIban() {
		new RentACar(null, NIF, null, VEHICLES_PRICE);
	}
	
	@Test(expected = CarException.class)
	public void nullNifAndIban() {
		new RentACar(NAME, null, null, VEHICLES_PRICE);
	}

	@Test(expected = CarException.class)
	public void nullArguments() {
		new RentACar(null, null, null, VEHICLES_PRICE);
	}
	
	@Test(expected = CarException.class)
	public void emptyNameAndNif() {
		new RentACar("", "", IBAN, VEHICLES_PRICE);
	}

	@Test(expected = CarException.class)
	public void emptyNameAndIban() {
		new RentACar("", NIF, "", VEHICLES_PRICE);
	}
	
	@Test(expected = CarException.class)
	public void emptyNifAndIban() {
		new RentACar(NAME, "", "", VEHICLES_PRICE);
	}

	@Test(expected = CarException.class)
	public void emptyArguments() {
		new RentACar("", "", "", VEHICLES_PRICE);
	} */

	@After
	public void tearDown() {
		RentACar.rentACars.clear();
	}
}
