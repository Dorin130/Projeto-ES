package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

public class BulkRoomBookingGetReferenceMethodTest {
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Hotel hotel;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Paris");
	}

	@Test
	public void failureDueToNoRooms() {
		BulkRoomBooking bulkRoomBooking = new BulkRoomBooking(1, begin, end);
		
		String reference = bulkRoomBooking.getReference("SINGLE");
		assertNull(reference);
		
		reference = bulkRoomBooking.getReference("DOUBLE");
		assertNull(reference);
	}
	
	@Test
	public void successTypeSingle() {
		new Room(hotel, "01", Type.SINGLE);
		new Room(hotel, "02", Type.DOUBLE);
		
		BulkRoomBooking bulkRoomBooking = new BulkRoomBooking(2, begin, end);
		bulkRoomBooking.processBooking();
		
		String reference = bulkRoomBooking.getReference("SINGLE");
		
		assertNotNull(reference);
		assertEquals("SINGLE", HotelInterface.getRoomBookingData(reference).getRoomType());
		assertFalse(bulkRoomBooking.getReferences().contains(reference));
	}
	
	@Test
	public void successTypeDouble() {
		new Room(hotel, "01", Type.SINGLE);
		new Room(hotel, "02", Type.DOUBLE);
		
		BulkRoomBooking bulkRoomBooking = new BulkRoomBooking(2, begin, end);
		bulkRoomBooking.processBooking();
		
		String reference = bulkRoomBooking.getReference("DOUBLE");
		
		assertNotNull(reference);
		assertEquals("DOUBLE", HotelInterface.getRoomBookingData(reference).getRoomType());
		assertFalse(bulkRoomBooking.getReferences().contains(reference));
	}

	@Test
	public void excludesInvalidReferences() {
		new Room(hotel, "01", Type.SINGLE);
		
		BulkRoomBooking bulkRoomBooking = new BulkRoomBooking(2, begin, end);
		bulkRoomBooking.processBooking();
		Hotel.hotels.clear();
		
		String reference = bulkRoomBooking.getReference("SINGLE");
		
		assertNull(reference);
	}
	
	@Test
	public void bookingCancelled() {
		BulkRoomBooking bulkRoomBooking = new BulkRoomBooking(1, begin, end);
		for(int i=0; i<BulkRoomBooking.MAX_HOTEL_EXCEPTIONS; i++) {
			bulkRoomBooking.processBooking();
		}
		
		String reference = bulkRoomBooking.getReference("SINGLE");
		
		assertNull(reference);
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
}
