package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

import static junit.framework.TestCase.assertTrue;

public class HotelReserveRoomMethodTest {
    private final LocalDate arrival = new LocalDate(2016, 12, 19);
    private final LocalDate departure = new LocalDate(2016, 12, 24);
    private final String NIF = "123456789";
	private final String IBAN = "PT50 1111 2222 3333 4444"; 
	private final int priceSingle = 50;
	private final int priceDouble = 100;
    private Room room;
    private Hotel hotel;
    private final String BuyerNIF = "111111111";
	private final String BuyerIBAN = "PT50 2222 3333 4444 5555";

    @Before
    public void setUp() {
        hotel = new Hotel("XPTO123", "Lisboa", this.NIF, this.IBAN, this.priceSingle, this.priceDouble);
        this.room = new Room(hotel, "01", Room.Type.SINGLE);
    }

    @Test
    public void success() {
        String ref = Hotel.reserveRoom(Room.Type.SINGLE, arrival, departure, this.BuyerNIF, this.BuyerIBAN);
        assertTrue(ref.startsWith("XPTO123"));
    }

    @Test(expected = HotelException.class)
    public void noHotels() {
        Hotel.hotels.clear();
        Hotel.reserveRoom(Room.Type.SINGLE, arrival, departure, this.BuyerNIF, this.BuyerIBAN);
    }

    @Test(expected = HotelException.class)
    public void noVacancy() {
        hotel.removeRooms();
        String ref = Hotel.reserveRoom(Room.Type.SINGLE, arrival, new LocalDate(2016, 12, 25), this.BuyerNIF, this.BuyerIBAN);
        System.out.println(ref);
    }

    @Test(expected = HotelException.class)
    public void noRooms() {
        hotel.removeRooms();
        Hotel.reserveRoom(Room.Type.SINGLE, arrival, new LocalDate(2016, 12, 25), this.BuyerNIF, this.BuyerIBAN);
    }
    
    
	@Test(expected = HotelException.class)
	public void nullBuyerNIF() {
		Hotel.reserveRoom(Room.Type.SINGLE, this.arrival, this.departure, null, this.BuyerIBAN);
	}
	@Test(expected = HotelException.class)
	public void badBuyerNIF() {
		Hotel.reserveRoom(Room.Type.SINGLE, this.arrival, this.departure, "", this.BuyerIBAN);
	}
	@Test(expected = HotelException.class)
	public void nullBuyerIBAN() {
		Hotel.reserveRoom(Room.Type.SINGLE, this.arrival, this.departure, this.BuyerNIF, null);
	}
	@Test(expected = HotelException.class)
	public void badBuyerIBAN() {
		Hotel.reserveRoom(Room.Type.SINGLE, this.arrival, this.departure, this.BuyerNIF, "");
	}

    @After
    public void tearDown() {
        Hotel.hotels.clear();
    }

}