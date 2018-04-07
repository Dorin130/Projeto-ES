package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelConstructorTest {
	private static final String HOTEL_NAME = "Londres";
	private static final String HOTEL_CODE = "XPTO123";
	private static final String NIF = "123456789";
	private static final String IBAN = "PT50 1111 2222 3333 4444"; 
	private final int priceSingle = 50;
	private final int priceDouble = 100;

	@Test
	public void success() {
		Hotel hotel = new Hotel(HOTEL_CODE, HOTEL_NAME, NIF, IBAN, this.priceSingle, this.priceDouble);

		Assert.assertEquals(HOTEL_NAME, hotel.getName());
		Assert.assertTrue(hotel.getCode().length() == Hotel.CODE_SIZE);
		Assert.assertEquals(0, hotel.getNumberOfRooms());
		Assert.assertEquals(1, Hotel.hotels.size());
		Assert.assertEquals(50, hotel.getPrice(Type.SINGLE)) ;
		Assert.assertEquals(100, hotel.getPrice(Type.DOUBLE) );
	}

	@Test(expected = HotelException.class)
	public void nullCode() {
		new Hotel(null, HOTEL_NAME , NIF, IBAN, this.priceSingle, this.priceDouble);
	}

	@Test(expected = HotelException.class)
	public void blankCode() {
		new Hotel("       ", HOTEL_NAME, NIF, IBAN, this.priceSingle, this.priceDouble);
	}

	@Test(expected = HotelException.class)
	public void emptyCode() {
		new Hotel("", HOTEL_NAME, NIF, IBAN, this.priceSingle, this.priceDouble);
	}

	@Test(expected = HotelException.class)
	public void nullName() {
		new Hotel(HOTEL_CODE, null, NIF, IBAN, this.priceSingle, this.priceDouble);
	}

	@Test(expected = HotelException.class)
	public void blankName() {
		new Hotel(HOTEL_CODE, "  ", NIF, IBAN, this.priceSingle, this.priceDouble);
	}

	@Test(expected = HotelException.class)
	public void emptyName() {
		new Hotel(HOTEL_CODE, "", NIF, IBAN, this.priceSingle, this.priceDouble);
	}

	@Test(expected = HotelException.class)
	public void codeSizeLess() {
		new Hotel("123456", HOTEL_NAME, NIF, IBAN, this.priceSingle, this.priceDouble);
	}

	@Test(expected = HotelException.class)
	public void codeSizeMore() {
		new Hotel("12345678", HOTEL_NAME, NIF, IBAN, this.priceSingle, this.priceDouble);
	} 
	 
	@Test(expected = HotelException.class)
	public void codeNotUnique() {
		new Hotel(HOTEL_CODE, HOTEL_NAME, NIF, IBAN, this.priceSingle, this.priceDouble);
		new Hotel(HOTEL_CODE, HOTEL_NAME + " City", NIF, IBAN, this.priceSingle, this.priceDouble);
	}  
	
	@Test(expected = HotelException.class)
	public void nullNif() {
		new Hotel(HOTEL_CODE, HOTEL_NAME , null, IBAN, this.priceSingle, this.priceDouble);
	}

	@Test(expected = HotelException.class)
	public void nifNotUnique() {
		new Hotel(HOTEL_CODE, HOTEL_NAME , NIF, IBAN, this.priceSingle, this.priceDouble);
		new Hotel("XPTO124", HOTEL_NAME + "city" , NIF, IBAN, this.priceSingle, this.priceDouble);
	}   
	
	@Test(expected = HotelException.class)
	public void badNif() {
		new Hotel(HOTEL_CODE, HOTEL_NAME , "       ", NIF, this.priceSingle, this.priceDouble);
	}
	
	@Test(expected = HotelException.class)
	public void nullIban() {
		new Hotel(HOTEL_CODE, HOTEL_NAME , NIF, null, this.priceSingle, this.priceDouble);
	}
	
	@Test(expected = HotelException.class)
	public void badIban() {
		new Hotel(HOTEL_CODE, HOTEL_NAME , NIF, "       ", this.priceSingle, this.priceDouble);
	}
	
	@Test(expected = HotelException.class)
	public void badSinglePrice() {
		new Hotel(HOTEL_CODE, HOTEL_NAME , NIF, IBAN, -1, this.priceDouble);
	}
	@Test(expected = HotelException.class)
	public void badDoublePrice() {
		new Hotel(HOTEL_CODE, HOTEL_NAME , NIF, IBAN, this.priceSingle, -1);
	}
	@Test(expected = HotelException.class)
	public void zeroSinglePrice() {
		new Hotel(HOTEL_CODE, HOTEL_NAME , NIF, IBAN, 0, this.priceDouble);
	}
	@Test(expected = HotelException.class)
	public void zeroDoublePrice() {
		new Hotel(HOTEL_CODE, HOTEL_NAME , NIF, IBAN, this.priceSingle, 0);
	} 
	
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
