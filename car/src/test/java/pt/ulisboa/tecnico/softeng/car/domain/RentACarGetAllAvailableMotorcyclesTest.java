package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RentACarGetAllAvailableMotorcyclesTest {
	private final LocalDate begin = new LocalDate(2018, 01, 01);
	private final LocalDate end = new LocalDate(2018, 01, 03);
	private final Set<Motorcycle> availableMotorcycles = new HashSet<>();
	private RentACar rentacar;
	private RentACar extraRentacar;
	
	@Before
	public void setUp() {		
		this.rentacar = new RentACar("CompanyName");
		this.extraRentacar = new RentACar("CompanyNameExtra");
		new Car("AA-01-BB", 400, this.rentacar);
		availableMotorcycles.add(new Motorcycle("CC-02-DD", 300, this.rentacar));
		availableMotorcycles.add(new Motorcycle("EE-03-FF", 200, this.rentacar));
		availableMotorcycles.add(new Motorcycle("GG-04-HH", 100, this.rentacar));
	}
	
	@Test
	public void success() {
		Set<Motorcycle> motorcycles = RentACar.getAllAvailableMotorcycles(begin, end);
		assertEquals(availableMotorcycles, motorcycles);
	}
	
	@Test
	public void multipleRentACars() {
		availableMotorcycles.add(new Motorcycle("II-05-JJ", 1500, extraRentacar));
		
		Set<Motorcycle> motorcycles = RentACar.getAllAvailableMotorcycles(begin, end);
		assertEquals(availableMotorcycles, motorcycles);
	}
	
	@Test
	public void oneMotorcycleRented() {
		Motorcycle motorcycle = new Motorcycle("LL-06-MM", 900, this.rentacar);
		motorcycle.rent("C778798", begin, end);
		
		Set<Motorcycle> motorcycles = RentACar.getAllAvailableMotorcycles(begin, end);
		assertEquals(availableMotorcycles, motorcycles);
	}
	
	@After
	public void tearDown() {
		RentACar.rentacars.clear();
		availableMotorcycles.clear();
		Vehicle.plates.clear();
	}	
}
