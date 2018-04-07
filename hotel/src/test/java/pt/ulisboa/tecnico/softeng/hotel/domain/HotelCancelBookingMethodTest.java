package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelCancelBookingMethodTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private final String NIF = "123456789";
	private final String IBAN = "PT50 1111 2222 3333 4444"; 
	private Hotel hotel;
	private Room room;
	private Booking booking;
	private final int priceSingle = 50;
	private final int priceDouble = 100;
	
	private final String BuyerNIF = "111111111";
	private final String BuyerIBAN = "PT50 2222 3333 4444 5555";

	@Before
	public void setUp() { 
		this.hotel = new Hotel("XPTO123", "Paris", this.NIF, this.IBAN, this.priceSingle, this.priceDouble);
		this.room = new Room(this.hotel, "01", Type.DOUBLE);
		this.booking = this.room.reserve(Type.DOUBLE, this.arrival, this.departure, this.BuyerNIF, this.BuyerIBAN);
	}

	@Test
	public void success() {
		String cancel = Hotel.cancelBooking(this.booking.getReference());

		Assert.assertTrue(this.booking.isCancelled());
		Assert.assertEquals(cancel, this.booking.getCancellation());
	}

	@Test(expected = HotelException.class)
	public void doesNotExist() {
		Hotel.cancelBooking("XPTO");
	}

	@Test(expected = HotelException.class)
	public void nullReference() {
		Hotel.cancelBooking(null);
	}

	@Test(expected = HotelException.class)
	public void emptyReference() {
		Hotel.cancelBooking("");
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
}
