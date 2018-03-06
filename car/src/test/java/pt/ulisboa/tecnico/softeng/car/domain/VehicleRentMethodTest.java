package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.fail;
import org.joda.time.LocalDate;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;


//Vehicle.rent(drivingLicense, begin, end)

public class VehicleRentMethodTest{
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 24);
	private final String drivingLicense = "A123";
	private Car car;

	@Before
	public void setUp() {
		RentACar rentACar = new RentACar("um nome");
		this.car = new Car("AA-AA-AA", 0, rentACar );
	}

	@Test
	public void success() {
		Renting renting = this.car.rent(this.drivingLicense, this.begin, this.begin);

		Assert.assertEquals(1, this.car.getNumberOfRentings());
		Assert.assertTrue(renting.getReference().length() > 0);
		Assert.assertEquals(this.begin, renting.getBegin());
		Assert.assertEquals(this.end, renting.getEnd());
	}

	@Test(expected = CarException.class)
	public void noDrivingLicence() {
		this.car.rent(null, this.begin, this.end);
	}

	@Test(expected = CarException.class)
	public void badLicense() {
		this.car.rent("123123", this.begin, this.end);
	}

	@Test(expected = CarException.class)
	public void nullEnd() {
		this.car.rent(this.drivingLicense, this.begin, null);
	}

	@Test(expected = CarException.class)
	public void nullBegin() {
		this.car.rent(this.drivingLicense, null, this.end);
	}
	
	@Test(expected = CarException.class)
	public void badTimes() {
		this.car.rent(this.drivingLicense, this.end, this.begin);
	}
	
	@Test
	public void allConflict() {
		this.car.rent(this.drivingLicense, this.begin, this.end);

		try {
			this.car.rent(this.drivingLicense, this.begin, this.end);
			fail();
		}
		catch (CarException ce) {
			Assert.assertEquals(1, this.car.getNumberOfRentings());
		}
	}

	@After
	public void tearDown() {
		car.getRentings().clear();
	}
}