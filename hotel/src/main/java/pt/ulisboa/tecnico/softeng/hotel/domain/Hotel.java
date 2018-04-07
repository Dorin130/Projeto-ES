package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Hotel {
	public static Set<Hotel> hotels = new HashSet<>(); 

	static final int CODE_SIZE = 7;

	private final String code;
	private final String name;
	private final String nif;
	private final String iban;
	private final int price_single;
	private final int price_double;
	private final Set<Room> rooms = new HashSet<>();
	
	private final Processor processor = new Processor();

	public Hotel(String code, String name, String nif, String iban, int price_single, int price_double) {
		checkArguments(code, name, nif, iban, price_single, price_double);

		this.code = code;
		this.name = name;
		this.nif = nif; 
		this.iban = iban;
		this.price_single = price_single;
		this.price_double = price_double;
		Hotel.hotels.add(this);
	}
 
	private void checkArguments(String code, String name, String nif, String iban, int price_single, int price_double) {
		if (code == null || name == null || code.trim().length() == 0 ||  name.trim().length() == 0  ||nif == null|| 
				 iban == null ||nif.trim().length() == 0 || iban.trim().length() == 0|| price_single <=0|| price_double<=0) {
			throw new HotelException(); 
		} 

		if (code.length() != Hotel.CODE_SIZE) {
			throw new HotelException();
		}

		for (Hotel hotel : hotels) {
			if (hotel.getCode().equals(code)) {
				throw new HotelException();
			}
		}
		
		for (Hotel hotel : hotels) {
			if (hotel.getNif().equals(nif)) {
				throw new HotelException();
			} 
		} 
	}

	public Room hasVacancy(Room.Type type, LocalDate arrival, LocalDate departure) {
		if (type == null || arrival == null || departure == null) {
			throw new HotelException();
		}

		for (Room room : this.rooms) {
			if (room.isFree(type, arrival, departure)) {
				return room;
			}
		}
		return null;
	}
	
	
	public int getPrice(Type type) {
		if(type == Type.SINGLE){
			return this.price_single;
		}
		else if(type == Type.DOUBLE) {
			return this.price_double;
		}
		return -1;
	}

	public String getCode() {
		return this.code;
	}

	public String getName() {
		return this.name;
	}

	public String getNif() {
		return this.nif;
	}

	public String getIban() {
		return this.iban;
	}
	
	void addRoom(Room room) {
		if (hasRoom(room.getNumber())) {
			throw new HotelException();
		}

		this.rooms.add(room);
	}

	int getNumberOfRooms() {
		return this.rooms.size();
	}

	public Processor getProcessor() {
		return this.processor;
	}
	
	public boolean hasRoom(String number) {
		for (Room room : this.rooms) {
			if (room.getNumber().equals(number)) {
				return true;
			}
		}
		return false;
	}

	private Booking getBooking(String reference) {
		for (Room room : this.rooms) {
			Booking booking = room.getBooking(reference);
			if (booking != null) {
				return booking;
			}
		}
		return null;
	}

	public static String reserveRoom(Room.Type type, LocalDate arrival, LocalDate departure, String nif, String iban) {
		for (Hotel hotel : Hotel.hotels) {
			Room room = hotel.hasVacancy(type, arrival, departure);
			if (room != null) {
				return room.reserve(type, arrival, departure, nif, iban).getReference();
			}
		}
		throw new HotelException();
	}

	public static String cancelBooking(String reference) {
		for (Hotel hotel : hotels) {
			Booking booking = hotel.getBooking(reference);
			if (booking != null) {
				return booking.cancel();
			}
		}
		throw new HotelException();
	}

	public static RoomBookingData getRoomBookingData(String reference) {
		for (Hotel hotel : hotels) {
			for (Room room : hotel.rooms) {
				Booking booking = room.getBooking(reference);
				if (booking != null) {
					return new RoomBookingData(room, booking);
				}
			}
		}
		throw new HotelException();
	}

	public static Set<String> bulkBooking(int number, LocalDate arrival, LocalDate departure, String buyerNif, String buyerIban) {
		if (number < 1) {
			throw new HotelException();
		}

		Set<Room> rooms = getAvailableRooms(number, arrival, departure);
		if (rooms.size() < number) {
			throw new HotelException();
		}

		Set<String> references = new HashSet<>();
		for (Room room : rooms) {
			references.add(room.reserve(room.getType(), arrival, departure, buyerNif, buyerIban).getReference());
		}

		return references;
	}

	static Set<Room> getAvailableRooms(int number, LocalDate arrival, LocalDate departure) {
		Set<Room> rooms = new HashSet<>();
		for (Hotel hotel : hotels) {
			for (Room room : hotel.rooms) {
				if (room.isFree(room.getType(), arrival, departure)) {
					rooms.add(room);
					if (rooms.size() == number) {
						return rooms;
					}
				}
			}
		}
		return rooms;
	}

    public void removeRooms() {
        rooms.clear();
    }
}
