package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

public class BulkRoomBookingProcessBookingTest {
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Hotel hotel;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Paris");
		new Room(hotel, "01", Type.SINGLE);
	}

	@Test
	public void success() {
		BulkRoomBooking bulkRoomBooking = new BulkRoomBooking(2, begin, end);
		new Room(hotel, "02", Type.SINGLE);
		
		bulkRoomBooking.processBooking();

		assertEquals(2, bulkRoomBooking.getReferences().size());
		
		for (String reference : bulkRoomBooking.getReferences()) {
			assertEquals(begin, HotelInterface.getRoomBookingData(reference).getArrival());
			assertEquals(end, HotelInterface.getRoomBookingData(reference).getDeparture());
		}
	}
	
	@Test
	public void successRoomAddedOnTime() {
		BulkRoomBooking bulkRoomBooking = new BulkRoomBooking(2, begin, end);
		for(int i=0; i<BulkRoomBooking.MAX_HOTEL_EXCEPTIONS-1; i++) {
			bulkRoomBooking.processBooking();
		}
		new Room(hotel, "02", Type.SINGLE);
		
		bulkRoomBooking.processBooking();

		assertEquals(2, bulkRoomBooking.getReferences().size());
	}
	
	@Test
	public void failureTooManyHotelExceptions() {
		BulkRoomBooking bulkRoomBooking = new BulkRoomBooking(2, begin, end);
		for(int i=0; i<BulkRoomBooking.MAX_HOTEL_EXCEPTIONS; i++) {
			bulkRoomBooking.processBooking();
		}
		new Room(hotel, "02", Type.SINGLE);
		
		bulkRoomBooking.processBooking();

		assertEquals(0, bulkRoomBooking.getReferences().size());
	}
	
	@Test
	public void allows1DayStayDuration() {
		BulkRoomBooking bulkRoomBooking = new BulkRoomBooking(1, begin, begin);
		
		bulkRoomBooking.processBooking();

		assertEquals(1, bulkRoomBooking.getReferences().size());
	}
	
	@Test
	public void allowsVariousRoomTypes() {
		BulkRoomBooking bulkRoomBooking = new BulkRoomBooking(2, begin, end);
		new Room(hotel, "02", Type.DOUBLE);
		
		bulkRoomBooking.processBooking();

		assertEquals(2, bulkRoomBooking.getReferences().size());
	}
	
	@Test
	public void prohibitsConflictingBookings() {
		BulkRoomBooking bulkRoomBooking1 = new BulkRoomBooking(1, begin, end);
		bulkRoomBooking1.processBooking();
		
		LocalDate begin2 = new LocalDate(2016, 12, 20);
		LocalDate end2 = new LocalDate(2016, 12, 22);
		BulkRoomBooking bulkRoomBooking2 = new BulkRoomBooking(1, begin2, end2);
		bulkRoomBooking2.processBooking();

		assertEquals(1, bulkRoomBooking1.getReferences().size());
		assertEquals(0, bulkRoomBooking2.getReferences().size());
	}
	
	@Test
	public void allowsMultipleNonConflictingBookings() {
		BulkRoomBooking bulkRoomBooking1 = new BulkRoomBooking(1, begin, end);
		bulkRoomBooking1.processBooking();
		
		LocalDate begin2 = new LocalDate(2016, 12, 23);
		LocalDate end2 = new LocalDate(2016, 12, 25);
		BulkRoomBooking bulkRoomBooking2 = new BulkRoomBooking(1, begin2, end2);
		bulkRoomBooking2.processBooking();

		assertEquals(1, bulkRoomBooking1.getReferences().size());
		assertEquals(1, bulkRoomBooking2.getReferences().size());
	}
	
	public void allowsRepeatingBookings() {
		BulkRoomBooking bulkRoomBooking = new BulkRoomBooking(1, begin, end);
		bulkRoomBooking.processBooking();
		bulkRoomBooking.processBooking();

		assertEquals(2, bulkRoomBooking.getReferences().size());
	}
	

	//assertEquals(Adventure.State.CONFIRMED, adventure.getState());
	//assertNotNull(adventure.getPaymentConfirmation());
	//assertNotNull(adventure.getActivityConfirmation());


	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
}
