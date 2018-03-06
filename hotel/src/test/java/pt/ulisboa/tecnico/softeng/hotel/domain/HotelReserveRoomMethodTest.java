package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

import static org.junit.Assert.fail;

public class HotelReserveRoomMethodTest {
    private static final String HOTEL_NAME = "Londres";
    private static final String HOTEL_CODE = "XPTO123";
    private final LocalDate arrival = new LocalDate(2018, 02, 19);
    private final LocalDate departure = new LocalDate(2018, 02, 24);

    private Hotel hotel;


    @Before
    public void setUp() {
        this.hotel = new Hotel(HOTEL_CODE, HOTEL_NAME);
        new Room(this.hotel, "01", Room.Type.DOUBLE);
        new Room(this.hotel, "02", Room.Type.SINGLE);
        new Room(this.hotel, "03", Room.Type.DOUBLE);
        new Room(this.hotel, "04", Room.Type.SINGLE);
    }

    @Test
    public void success() {
        Hotel.hotels.clear();
        Room room = new Room(this.hotel, "01", Room.Type.SINGLE);
        String reference = this.hotel.reserveRoom(Room.Type.SINGLE, arrival, departure);
        RoomBookingData data = this.hotel.getRoomBookingData(reference);

        Assert.assertEquals(room.getHotel().getCode(), data.getHotelCode());
        Assert.assertEquals(room.getNumber(), data.getRoomNumber());
        Assert.assertEquals(departure, data.getDeparture());
        Assert.assertEquals(arrival, data.getArrival());

    }

    @Test(expected = HotelException.class)
    public void noRoom() {
        this.hotel.hotels.clear();
        this.hotel = new Hotel(HOTEL_CODE, HOTEL_NAME);
        this.hotel.reserveRoom(Room.Type.SINGLE, arrival, departure);

    }

    @Test(expected = HotelException.class)
    public void nullType() {
        this.hotel.reserveRoom(null, arrival, departure);
    }

    @Test(expected = HotelException.class)
    public void nullArrival() {
        this.hotel.reserveRoom(Room.Type.SINGLE, null, departure);
    }

    @Test(expected = HotelException.class)
    public void nullDeparture() {
        this.hotel.reserveRoom(Room.Type.SINGLE, arrival, null);
    }

    @Test(expected = HotelException.class)
    public void arrivalAfterDeparture() {
        this.hotel.reserveRoom(Room.Type.SINGLE, departure, arrival);
    }

    @Test(expected = HotelException.class)
    public void arrivalEqualsDeparture() {
        this.hotel.reserveRoom(Room.Type.SINGLE, arrival, arrival);
    }

    @After
    public void tearDown() {
        Hotel.hotels.clear();
    }


}
