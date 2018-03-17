package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RentACarGetAllAvailableCarsTest {
	private final LocalDate begin = new LocalDate(2018, 01, 01);
	private final LocalDate end = new LocalDate(2018, 01, 03);
	private final Set<Car> availableCars = new HashSet<>();
	private RentACar rentacar;
	private RentACar extraRentacar;
	
	@Before
	public void setUp() {		
		this.rentacar = new RentACar("CompanyName");
		this.extraRentacar = new RentACar("CompanyNameExtra");
		new Motorcycle("AA-01-BB", 400, this.rentacar);
		availableCars.add(new Car("CC-02-DD", 300, this.rentacar));
		availableCars.add(new Car("EE-03-FF", 200, this.rentacar));
		availableCars.add(new Car("GG-04-HH", 100, this.rentacar));
	}
	
	@Test
	public void success() {
		Set<Car> cars = RentACar.getAllAvailableCars(begin, end);
		assertEquals(availableCars, cars);
	}
	
	@Test
	public void multipleRentACars() {
		availableCars.add(new Car("II-05-JJ", 1500, extraRentacar));
		
		Set<Car> cars = RentACar.getAllAvailableCars(begin, end);
		assertEquals(availableCars, cars);
	}
	
	@After
	public void tearDown() {
		RentACar.rentacars.clear();
		availableCars.clear();
		Vehicle.plates.clear();
	}	
}
