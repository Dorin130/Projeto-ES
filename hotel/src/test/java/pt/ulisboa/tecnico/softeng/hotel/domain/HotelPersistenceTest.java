package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

public class HotelPersistenceTest {
	private static final double HOTEL_PRICE_DOUBLE = 20.0;
	private static final double HOTEL_PRICE_SINGLE = 10.0;
	private static final String HOTEL_IBAN = "IBAN";
	private static final String HOTEL_NIF = "123456789";
	private static final String HOTEL_NAME = "Berlin Plaza";
	private final static String HOTEL_CODE = "H123456";
	private static final String ROOM_NUMBER = "01";

	private final LocalDate arrival = new LocalDate(2017, 12, 15);
	private final LocalDate departure = new LocalDate(2017, 12, 19);

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		Hotel hotel = new Hotel(HOTEL_CODE, HOTEL_NAME, HOTEL_NIF, HOTEL_IBAN, HOTEL_PRICE_SINGLE, HOTEL_PRICE_DOUBLE);

		Room room = new Room(hotel, ROOM_NUMBER, Type.DOUBLE);

		room.reserve(Type.DOUBLE, this.arrival, this.departure, HOTEL_NIF, HOTEL_IBAN);

	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		Hotel hotel = Hotel.getHotelByCode(HOTEL_CODE);

		assertEquals(HOTEL_NAME, hotel.getName());
		assertEquals(HOTEL_CODE, hotel.getCode());
		assertEquals(HOTEL_NIF, hotel.getNif());
		assertEquals(HOTEL_IBAN, hotel.getIban());
		assertEquals(HOTEL_PRICE_SINGLE, hotel.getPriceSingle(), 0.0);
		assertEquals(HOTEL_PRICE_DOUBLE, hotel.getPriceDouble(), 0.0);
		assertEquals(1, hotel.getRoomSet().size());

		List<Room> hotels = new ArrayList<>(hotel.getRoomSet());
		Room room = hotels.get(0);

		assertEquals(ROOM_NUMBER, room.getNumber());
		assertEquals(Type.DOUBLE, room.getType());
		assertEquals(1, room.getBookingSet().size());

		List<Booking> bookings = new ArrayList<>(room.getBookingSet());
		Booking booking = bookings.get(0);

		assertEquals(this.arrival, booking.getArrival());
		assertEquals(this.departure, booking.getDeparture());
		assertNotNull(booking.getReference());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			hotel.delete();
		}
	}

}
