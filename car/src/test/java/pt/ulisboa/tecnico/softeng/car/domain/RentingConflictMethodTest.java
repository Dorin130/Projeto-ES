package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentingConflictMethodTest {
	private final LocalDate begin = new LocalDate(2018, 12, 15);
	private final LocalDate end = new LocalDate(2018, 12, 20);
	private RentACar rentacar;
	private Renting renting;
	
	@Before
	public void setUp() {
		rentacar = new RentACar("CompanyName");
		renting = new Renting(this.rentacar, "123456", this.begin, this.end);
	}
	
	@Test
	public void argumentsAreConsistent() {
		assertFalse(renting.conflict(new LocalDate(2018, 12, 5), new LocalDate(2018, 12, 14)));
	}
	
	@Test
	public void noConflictBecauseItIsCancelled() {
		this.renting.cancel();
		Assert.assertFalse(this.renting.conflict(this.renting.getBegin() , this.renting.getEnd()));
	}
	
	@Test(expected = CarException.class)
	public void argumentsAreInconsistent() {
		this.renting.conflict(new LocalDate(2018, 12, 15), new LocalDate(2018, 12, 9));
	}
	
	@Test
	public void argumentsSameDay() {
		Assert.assertTrue(this.renting.conflict(new LocalDate(2016, 12, 9), new LocalDate(2016, 12, 9)));
	}
	
	@Test
	public void arrivalAndDepartureAreBeforeBooked() {
		Assert.assertFalse(this.renting.conflict(this.begin.minusDays(10), this.begin.minusDays(4)));
	}

	@Test
	public void arrivalAndDepartureAreBeforeBookedButDepartureIsEqualToBookedArrival() {
		Assert.assertFalse(this.renting.conflict(this.begin.minusDays(10), this.begin));
	}

	@Test
	public void arrivalAndDepartureAreAfterBooked() {
		Assert.assertFalse(this.renting.conflict(this.end.plusDays(4), this.end.plusDays(10)));
	}

	@Test
	public void arrivalAndDepartureAreAfterBookedButArrivalIsEqualToBookedDeparture() {
		Assert.assertFalse(this.renting.conflict(this.end, this.end.plusDays(10)));
	}

	@Test
	public void arrivalIsBeforeBookedArrivalAndDepartureIsAfterBookedDeparture() {
		Assert.assertTrue(this.renting.conflict(this.begin.minusDays(4), this.end.plusDays(4)));
	}

	@Test
	public void arrivalIsEqualBookedArrivalAndDepartureIsAfterBookedDeparture() {
		Assert.assertTrue(this.renting.conflict(this.begin, this.end.plusDays(4)));
	}

	@Test
	public void arrivalIsBeforeBookedArrivalAndDepartureIsEqualBookedDeparture() {
		Assert.assertTrue(this.renting.conflict(this.begin.minusDays(4), this.end));
	}

	@Test
	public void arrivalIsBeforeBookedArrivalAndDepartureIsBetweenBooked() {
		Assert.assertTrue(this.renting.conflict(this.begin.minusDays(4), this.end.minusDays(3)));
	}

	@Test
	public void arrivalIsBetweenBookedAndDepartureIsAfterBookedDeparture() {
		Assert.assertTrue(this.renting.conflict(this.begin.plusDays(3), this.end.plusDays(6)));
	}


	@After
	public void tearDown() {
		RentACar.rentacars.clear();
	}
	
}
