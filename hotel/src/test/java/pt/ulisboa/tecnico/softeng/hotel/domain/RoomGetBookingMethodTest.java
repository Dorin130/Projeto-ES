package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

public class RoomGetBookingMethodTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 24);
	private final String NIF = "123456789";
	private final String IBAN = "PT50 1111 2222 3333 4444"; 
	private final int priceSingle = 50;
	private final int priceDouble = 100;
	private Hotel hotel;
	private Room room;
	private Booking booking;
	
	private final String BuyerNIF = "111111111";
	private final String BuyerIBAN = "PT50 2222 3333 4444 5555";

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Lisboa",this.NIF, this.IBAN, this.priceSingle, this.priceDouble);
		this.room = new Room(this.hotel, "01", Type.SINGLE);
		this.booking = this.room.reserve(Type.SINGLE, this.arrival, this.departure, this.BuyerNIF, this.BuyerIBAN);
	}

	@Test
	public void success() {
		assertEquals(this.booking, this.room.getBooking(this.booking.getReference()));
	}

	@Test
	public void successCancelled() {
		this.booking.cancel();

		assertEquals(this.booking, this.room.getBooking(this.booking.getCancellation()));
	}

	@Test
	public void doesNotExist() {
		assertNull(this.room.getBooking("XPTO"));
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
}
