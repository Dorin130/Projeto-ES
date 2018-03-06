package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RentACarGetAllAvailableCarsTest {
	private final LocalDate begin = new LocalDate(2018, 01, 01);
	private final LocalDate end = new LocalDate(2018, 01, 03);
	private RentACar rentacar;
	
	@Before
	public void setUp() {
		this.rentacar = new RentACar("CompanyName");
		new Car("AA-01-BB", 400, this.rentacar);
		new Car("CC-02-DD", 300, this.rentacar);
		new Car("EE-03-FF", 200, this.rentacar);
		new Car("GG-04-HH", 100, this.rentacar);
	}
}
