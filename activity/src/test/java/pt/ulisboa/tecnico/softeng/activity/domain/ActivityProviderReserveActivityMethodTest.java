package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class ActivityProviderReserveActivityMethodTest {
	private static final int AGE = 30;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);

	private ActivityProvider provider;
	private Activity activity;
	
	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		this.activity = new Activity(this.provider, "Bush Walking", 18, 70, 20);
	}

	@Test
	public void success() {
		ActivityOffer offer = new ActivityOffer(this.activity, this.begin, this.end);
		String reference = ActivityProvider.reserveActivity(begin, end, AGE);
		
		Assert.assertNotNull(offer.getBooking(reference));
	}

	@Test(expected = ActivityException.class)
	public void nullBeginDate() {
		ActivityProvider.reserveActivity(null, this.end, AGE);

	}

	@Test(expected = ActivityException.class)
	public void nullEndDate() {
		ActivityProvider.reserveActivity(this.begin, null, AGE);

	}
	
	@Test(expected = ActivityException.class)
	public void notExistOffers() {
		ActivityProvider.reserveActivity(begin, end, AGE);
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}
}