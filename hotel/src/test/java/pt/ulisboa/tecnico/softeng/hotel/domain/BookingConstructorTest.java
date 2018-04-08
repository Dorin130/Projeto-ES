package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class BookingConstructorTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private final String NIF = "123456789";
	private final String IBAN = "PT50 1111 2222 3333 4444"; 
	private Hotel hotel;
	private final int priceSingle = 50;
	private final int priceDouble = 100;
	private final String BuyerNIF = "111111111";
	private final String BuyerIBAN = "PT50 2222 3333 4444 5555";
	private final int PRICE = 50;
	private final Type type = Type.SINGLE;

	@Before
	public void setUp() {
		
		this.hotel = new Hotel("XPTO123", "Londres", this.NIF, this.IBAN, this.priceSingle, this.priceDouble);
	}

	@Test
	public void success() {
		Booking booking = new Booking(this.hotel, this.arrival, this.departure, this.BuyerNIF ,this.BuyerIBAN ,this.hotel.getPrice(this.type));

		Assert.assertTrue(booking.getReference().startsWith(this.hotel.getCode()));
		Assert.assertTrue(booking.getReference().length() > Hotel.CODE_SIZE);
		Assert.assertEquals(this.arrival, booking.getArrival());
		Assert.assertEquals(this.departure, booking.getDeparture());
	}

	@Test(expected = HotelException.class)
	public void nullHotel() {
		new Booking(null, this.arrival, this.departure, this.BuyerNIF ,this.BuyerIBAN , this.PRICE);
	}

	@Test(expected = HotelException.class)
	public void nullArrival() {
		new Booking(this.hotel, null, this.departure, this.BuyerNIF, this.BuyerIBAN , this.PRICE);
	}

	@Test(expected = HotelException.class)
	public void nullDeparture() {
		new Booking(this.hotel, this.arrival, null,this.BuyerNIF,this.BuyerIBAN , this.PRICE);
	}

	@Test(expected = HotelException.class)
	public void departureBeforeArrival() {
		new Booking(this.hotel, this.arrival, this.arrival.minusDays(1), this.BuyerNIF,this.BuyerIBAN , this.PRICE);
	}

	@Test
	public void arrivalEqualDeparture() {
		new Booking(this.hotel, this.arrival, this.arrival,  this.BuyerNIF , this.BuyerIBAN , this.PRICE);
	}
	
	
	@Test(expected = HotelException.class)
	public void nullBuyerNif() {
		new Booking(this.hotel, this.arrival, this.departure, null , this.BuyerIBAN , this.PRICE);
	}
	
	@Test(expected = HotelException.class)
	public void badBuyerNif() {
		new Booking(this.hotel, this.arrival, this.departure, "" , this.BuyerIBAN , this.PRICE);
	}
	
	@Test(expected = HotelException.class)
	public void nullBuyerIban() {
		new Booking(this.hotel, this.arrival, this.departure, this.BuyerNIF, null  , this.PRICE);
	}
	
	@Test(expected = HotelException.class)
	public void badBuyerIban() {
		new Booking(this.hotel, this.arrival, this.departure, this.BuyerNIF, "" , this.PRICE);
	}
	
	@Test(expected = HotelException.class)
	public void negPrice() {
		new Booking(this.hotel, this.arrival, this.departure, this.BuyerNIF, this.BuyerIBAN , -10);
	}
	
	@Test(expected = HotelException.class)
	public void zeroPrice() {
		new Booking(this.hotel, this.arrival, this.departure, this.BuyerNIF, this.BuyerIBAN , 0);
	}
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
