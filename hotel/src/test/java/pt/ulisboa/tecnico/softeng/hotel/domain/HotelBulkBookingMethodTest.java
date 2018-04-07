package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelBulkBookingMethodTest { 
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private final String NIF = "123456789";
	private final String IBAN = "PT50 1111 2222 3333 4444"; 
	private Hotel hotel;
	private final int priceSingle = 50;
	private final int priceDouble = 100;
	
	private final String OTHERNIF = "234567891";
	private final String OTHERIBAN = "PT50 2222 3333 4444 1111"; 
	
	private final String BuyerNIF = "111111111";
	private final String BuyerIBAN = "PT50 2222 3333 4444 5555";

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Paris", this.NIF, this.IBAN, this.priceSingle, this.priceDouble);
		new Room(this.hotel, "01", Type.DOUBLE);
		new Room(this.hotel, "02", Type.SINGLE);
		new Room(this.hotel, "03", Type.DOUBLE);
		new Room(this.hotel, "04", Type.SINGLE);

		this.hotel = new Hotel("XPTO124", "Paris", this.OTHERNIF, this.OTHERIBAN, this.priceSingle, this.priceDouble);
		new Room(this.hotel, "01", Type.DOUBLE);
		new Room(this.hotel, "02", Type.SINGLE);
		new Room(this.hotel, "03", Type.DOUBLE);
		new Room(this.hotel, "04", Type.SINGLE);
	}

	@Test
	public void success() {
		Set<String> references = Hotel.bulkBooking(2, this.arrival, this.departure, this.BuyerNIF, this.BuyerIBAN);

		assertEquals(2, references.size());
	}

	@Test(expected = HotelException.class)
	public void zeroNumber() {
		Hotel.bulkBooking(0, this.arrival, this.departure, this.BuyerNIF, this.BuyerIBAN);
	}

	@Test(expected = HotelException.class)
	public void noRooms() {
		Hotel.hotels.clear();
		this.hotel = new Hotel("XPTO124", "Paris",this.NIF, this.IBAN, this.priceSingle, this.priceDouble);

		Hotel.bulkBooking(3, this.arrival, this.departure, this.BuyerNIF, this.BuyerIBAN);
	}

	@Test
	public void OneNumber() {
		Set<String> references = Hotel.bulkBooking(1, this.arrival, this.departure, this.BuyerNIF, this.BuyerIBAN);

		assertEquals(1, references.size());
	}

	@Test(expected = HotelException.class)
	public void nullArrival() {
		Hotel.bulkBooking(2, null, this.departure, this.BuyerNIF, this.BuyerIBAN);
	}

	@Test(expected = HotelException.class)
	public void nullDeparture() {
		Hotel.bulkBooking(2, this.arrival, null, this.BuyerNIF, this.BuyerIBAN);
	}

	@Test
	public void reserveAll() {
		Set<String> references = Hotel.bulkBooking(8, this.arrival, this.departure, this.BuyerNIF, this.BuyerIBAN);

		assertEquals(8, references.size());
	}
	
	@Test(expected = HotelException.class)
	public void nullBuyerNIF() {
		Hotel.bulkBooking(0, this.arrival, this.departure, null, this.BuyerIBAN);
	}
	@Test(expected = HotelException.class)
	public void badBuyerNIF() {
		Hotel.bulkBooking(0, this.arrival, this.departure, "", this.BuyerIBAN);
	}
	@Test(expected = HotelException.class)
	public void nullBuyerIBAN() {
		Hotel.bulkBooking(0, this.arrival, this.departure, this.BuyerNIF, null);
	}
	@Test(expected = HotelException.class)
	public void badBuyerIBAN() {
		Hotel.bulkBooking(0, this.arrival, this.departure, this.BuyerNIF, "");
	}

	@Test
	public void reserveAllPlusOne() {
		try {
			Hotel.bulkBooking(9, this.arrival, this.departure, this.BuyerNIF, this.BuyerIBAN);
			fail();
		} catch (HotelException he) {
			assertEquals(8, Hotel.getAvailableRooms(8, this.arrival, this.departure).size());
		}
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
