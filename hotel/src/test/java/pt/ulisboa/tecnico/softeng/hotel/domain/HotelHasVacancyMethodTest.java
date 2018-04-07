package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertNull;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelHasVacancyMethodTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private final String NIF = "123456789";
	private final String IBAN = "PT50 1111 2222 3333 4444";
	private final String OTHERNIF = "234567891";
	private final String OTHERIBAN = "PT50 2222 3333 4444 1111"; 
	private final int priceSingle = 50;
	private final int priceDouble = 100;
	private Hotel hotel;
	private Room room;
	private final String BuyerNIF = "111111111";
	private final String BuyerIBAN = "PT50 2222 3333 4444 5555";

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Paris",this.NIF, this.IBAN, this.priceSingle, this.priceDouble);
		this.room = new Room(this.hotel, "01", Type.DOUBLE);
	}

	@Test
	public void hasVacancy() {
		Room room = this.hotel.hasVacancy(Type.DOUBLE, this.arrival, this.departure);

		Assert.assertNotNull(room);
		Assert.assertEquals("01", room.getNumber());
	}

	@Test
	public void noVacancy() {
		this.room.reserve(Type.DOUBLE, this.arrival, this.departure, this.BuyerNIF, this.BuyerIBAN);

		assertNull(this.hotel.hasVacancy(Type.DOUBLE, this.arrival, this.departure));
	}

	@Test
	public void noVacancyEmptyRoomSet() {
		Hotel otherHotel = new Hotel("XPTO124", "Paris Germain", this.OTHERNIF, this.OTHERIBAN, this.priceSingle, this.priceDouble);

		assertNull(otherHotel.hasVacancy(Type.DOUBLE, this.arrival, this.departure));
	} 

	@Test(expected = HotelException.class)
	public void nullType() {
		this.hotel.hasVacancy(null, this.arrival, this.departure);
	}

	@Test(expected = HotelException.class)
	public void nullArrival() {
		this.hotel.hasVacancy(Type.DOUBLE, null, this.departure);
	}

	@Test(expected = HotelException.class)
	public void nullDeparture() {
		this.hotel.hasVacancy(Type.DOUBLE, this.arrival, null);
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
