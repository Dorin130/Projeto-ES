package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
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
	private static final String TESTSTRING = "PERSISTENCETEST";

	private final LocalDate arrival = new LocalDate(2017, 12, 15);
	private final LocalDate departure = new LocalDate(2017, 12, 19);
	private static final LocalDate canceldate = LocalDate.parse("2018-03-06");

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		Hotel hotel = new Hotel(HOTEL_CODE, HOTEL_NAME, HOTEL_NIF, HOTEL_IBAN, HOTEL_PRICE_SINGLE, HOTEL_PRICE_DOUBLE);

		Room room = new Room(hotel, ROOM_NUMBER, Type.DOUBLE);

		Booking booking1 = room.reserve(Type.DOUBLE, this.arrival, this.departure, HOTEL_NIF, HOTEL_IBAN);
		
        /* Using setters to test persistence */
		booking1.setCancellation(TESTSTRING);
		booking1.setPaymentReference(TESTSTRING);
		booking1.setCancelledPaymentReference(TESTSTRING);
		booking1.setCancellationDate(canceldate);
		booking1.setInvoiceReference(TESTSTRING);

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
		
		assertEquals(HOTEL_NIF, booking.getProviderNif());
		assertEquals(false, booking.getCancelledInvoice());
		assertEquals(HOTEL_NIF, booking.getNif());
		assertEquals(HOTEL_IBAN, booking.getBuyerIban());
		assertEquals(this.arrival, booking.getArrival());
		assertEquals(this.departure, booking.getDeparture());
		assertNotNull(booking.getReference());	
		assertEquals(TESTSTRING , booking.getCancellation());
		assertEquals(TESTSTRING , booking.getPaymentReference());
		assertEquals(TESTSTRING , booking.getCancelledPaymentReference());
		assertEquals(TESTSTRING , booking.getInvoiceReference());
		assertEquals(canceldate , booking.getCancellationDate());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			hotel.delete();
		}
	}

}
