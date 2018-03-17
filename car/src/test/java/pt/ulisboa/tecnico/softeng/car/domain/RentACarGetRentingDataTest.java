package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentACarGetRentingDataTest {
	private final LocalDate begin = new LocalDate(2018, 01, 01);
	private final LocalDate end = new LocalDate(2018, 01, 03);
	private RentACar rentacar;
	private Vehicle vehicle;
	private Renting renting;
	
	@Before
	public void setUp() {
		this.rentacar = new RentACar("CompanyName");
		this.vehicle = new Vehicle("AA-01-BB", 100, this.rentacar);
		this.renting = this.vehicle.rent("A778798", this.begin, this.end);
	}
	
	@Test
	public void success() {
		RentingData rentingData = RentACar.getRentingData(this.renting.getReference());
		
		assertEquals(this.renting.getReference(), rentingData.getReference());
		assertEquals(this.rentacar.getCode(), rentingData.getRentACarCode());
		assertEquals(this.vehicle.getPlate(), rentingData.getPlate());
		assertEquals(this.renting.getBegin(), rentingData.getBegin());
		assertEquals(this.renting.getEnd(), rentingData.getEnd());
		assertEquals(this.renting.getDrivingLicense(), rentingData.getDrivingLicense());		
	}
	
	@Test(expected = CarException.class)
	public void nullReference() {
		RentACar.getRentingData(null);
	}
	
	@Test(expected = CarException.class)
	public void emptyReference() {
		RentACar.getRentingData("");
	}
	
	@Test(expected = CarException.class)
	public void referenceDoesNotExist() {
		RentACar.getRentingData("123a");
	}
	
	@After
	public void tearDown() {
		RentACar.rentacars.clear();
		Vehicle.plates.clear();
	}
}
