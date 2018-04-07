package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.fail;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class RoomReserveMethodTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 24);
	private final String NIF = "123456789";
	private final String IBAN = "PT50 1111 2222 3333 4444"; 
	private final int priceSingle = 50;
	private final int priceDouble = 100;
	private Room room;
	
	private final String BuyerNIF = "111111111";
	private final String BuyerIBAN = "PT50 2222 3333 4444 5555";

	@Before
	public void setUp() {
		Hotel hotel = new Hotel("XPTO123", "Lisboa",this.NIF, this.IBAN, this.priceSingle, this.priceDouble);
		this.room = new Room(hotel, "01", Type.SINGLE);
	}

	@Test
	public void success() {
		Booking booking = this.room.reserve(Type.SINGLE, this.arrival, this.departure, this.BuyerNIF, this.BuyerIBAN);

		Assert.assertEquals(1, this.room.getNumberOfBookings());
		Assert.assertTrue(booking.getReference().length() > 0);
		Assert.assertEquals(this.arrival, booking.getArrival());
		Assert.assertEquals(this.departure, booking.getDeparture());
	}

	@Test(expected = HotelException.class)
	public void noDouble() {
		this.room.reserve(Type.DOUBLE, this.arrival, this.departure, this.BuyerNIF, this.BuyerIBAN);
	}

	@Test(expected = HotelException.class)
	public void nullType() {
		this.room.reserve(null, this.arrival, this.departure, this.BuyerNIF, this.BuyerIBAN);
	}

	@Test(expected = HotelException.class)
	public void nullArrival() {
		this.room.reserve(Type.SINGLE, null, this.departure, this.BuyerNIF, this.BuyerIBAN);
	}

	@Test(expected = HotelException.class)
	public void nullDeparture() {
		this.room.reserve(Type.SINGLE, this.arrival, null, this.BuyerNIF, this.BuyerIBAN);
	}

	@Test(expected = HotelException.class)
	public void nullBuyerNIF() {
		this.room.reserve(Room.Type.SINGLE, this.arrival, this.departure, null, this.BuyerIBAN);
	}
	@Test(expected = HotelException.class)
	public void badBuyerNIF() {
		this.room.reserve(Room.Type.SINGLE, this.arrival, this.departure, "", this.BuyerIBAN);
	}
	@Test(expected = HotelException.class)
	public void nullBuyerIBAN() {
		this.room.reserve(Room.Type.SINGLE, this.arrival, this.departure, this.BuyerNIF, null);
	}
	@Test(expected = HotelException.class)
	public void badBuyerIBAN() {
		this.room.reserve(Room.Type.SINGLE, this.arrival, this.departure, this.BuyerNIF, "");
	}

	
	@Test
	public void allConflict() {
		this.room.reserve(Type.SINGLE, this.arrival, this.departure, this.BuyerNIF, this.BuyerIBAN);

		try {
			this.room.reserve(Type.SINGLE, this.arrival, this.departure, this.BuyerNIF, this.BuyerIBAN);
			fail();
		} catch (HotelException he) {
			Assert.assertEquals(1, this.room.getNumberOfBookings());
		}
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
}
