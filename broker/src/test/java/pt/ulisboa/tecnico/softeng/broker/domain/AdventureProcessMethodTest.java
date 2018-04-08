package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

@RunWith(JMockit.class)
public class AdventureProcessMethodTest {
	private static final String HOTEL_NAME = "Londres";
	private static final String HOTEL_CODE = "XPTO123";
	private static final String NIF = "123456789";
	private static final int AMOUNT = 200;
	private static final boolean NOT_RENT_CAR = false;
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private static final String VEHICLE_REFERENCE = "vehicleReference";
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String TAX_CONFIRMATION = "TaxConfirmation";
	private static final String DRIVING_LICENSE = "IMT1234";
	private final int priceSingle = 50;
	private final int priceDouble = 100;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private static final String buyerNIF = "123456789";
	private static final String sellerNIF = "987654321";
	private Broker broker;
	private String IBAN;
	pt.ulisboa.tecnico.softeng.broker.domain.Client clientBroker;

	@Before
	public void setUp() {
		this.broker = new Broker("BR01", "eXtremeADVENTURE", buyerNIF, sellerNIF);

		Bank bank = new Bank("Money", "BK01");
		Client client = new Client(bank, "António");
		Account account = new Account(bank, client);
		this.IBAN = account.getIBAN();
		account.deposit(1000);

		Hotel hotel = new Hotel(HOTEL_CODE, HOTEL_NAME, NIF, IBAN, this.priceSingle, this.priceDouble);
		new Room(hotel, "01", Type.SINGLE);

		clientBroker = new pt.ulisboa.tecnico.softeng.broker.domain.Client(this.broker, IBAN, NIF, DRIVING_LICENSE,20);

		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure", "NIF", "IBAN");
		Activity activity = new Activity(provider, "Bush Walking", 18, 80, 10);
		new ActivityOffer(activity, this.begin, this.end, 30);
		new ActivityOffer(activity, this.begin, this.begin, 30);
	}

	@Test
	public void success(@Mocked BankInterface bankInterface, @Mocked TaxInterface taxInterface,
						@Mocked CarInterface carInterface, @Mocked HotelInterface hotelInterface) {
		new Expectations() {
			{
				HotelInterface.reserveRoom(Type.SINGLE, begin, end, buyerNIF, IBAN);
				this.result = ROOM_CONFIRMATION;

				CarInterface.processVehicleRenting(DRIVING_LICENSE, begin, end, NIF, IBAN);
				this.result = VEHICLE_REFERENCE;

				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				TaxInterface.submitInvoice(null, null, anyString, AMOUNT, new LocalDate());
				this.result =  TAX_CONFIRMATION;
			}
		};
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, this.clientBroker, AMOUNT, true);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		assertEquals(Adventure.State.CONFIRMED, adventure.getState());
		assertNotNull(adventure.getActivityConfirmation());
		assertNotNull(adventure.getRoomConfirmation());
		assertNotNull(adventure.getVehicleConfirmation());
		assertNotNull(adventure.getPaymentConfirmation());
	}

	@Test
	public void successNoHotelBooking(@Mocked BankInterface bankInterface, @Mocked TaxInterface taxInterface,
									  @Mocked CarInterface carInterface) {
		new Expectations() {
			{
				CarInterface.processVehicleRenting(DRIVING_LICENSE, begin, begin, NIF, IBAN);
				this.result = VEHICLE_REFERENCE;

				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				TaxInterface.submitInvoice(null, null, anyString, AMOUNT, new LocalDate());
				this.result =  TAX_CONFIRMATION;
			}
		};
		Adventure adventure = new Adventure(this.broker, this.begin, this.begin, this.clientBroker, AMOUNT, true);

		adventure.process();
		adventure.process();
		adventure.process();

		assertEquals(Adventure.State.CONFIRMED, adventure.getState());
		assertNotNull(adventure.getActivityConfirmation());
		assertNotNull(adventure.getVehicleConfirmation());
		assertNotNull(adventure.getPaymentConfirmation());
	}
	
	@Test
	public void successNoVehicleBooking(@Mocked BankInterface bankInterface, @Mocked TaxInterface taxInterface,
										@Mocked HotelInterface hotelInterface) {
		new Expectations() {
			{
				HotelInterface.reserveRoom(Type.SINGLE, begin, end, buyerNIF, IBAN);
				this.result = ROOM_CONFIRMATION;

				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				TaxInterface.submitInvoice(null, null, anyString, AMOUNT, new LocalDate());
				this.result =  TAX_CONFIRMATION;
			}
		};
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, this.clientBroker, AMOUNT, false);

		adventure.process();
		adventure.process();
		adventure.process();

		assertEquals(Adventure.State.CONFIRMED, adventure.getState());
		//assertNotNull(adventure.getActivityConfirmation());
		//assertNotNull(adventure.getVehicleConfirmation());
		//assertNotNull(adventure.getPaymentConfirmation());
	}
	
	@Test
	public void successNoRoomNoVehicleBooking(@Mocked BankInterface bankInterface, @Mocked TaxInterface taxInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, 200);
				this.result = PAYMENT_CONFIRMATION;

				TaxInterface.submitInvoice(null, null, anyString, AMOUNT, new LocalDate());
				this.result =  TAX_CONFIRMATION;
			}
		};
		Adventure adventure = new Adventure(this.broker, this.begin, this.begin, this.clientBroker, AMOUNT, false);

		adventure.process();
		adventure.process();

		assertEquals(Adventure.State.CONFIRMED, adventure.getState());
		assertNotNull(adventure.getActivityConfirmation());
		assertNotNull(adventure.getPaymentConfirmation());
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
		Hotel.hotels.clear();
		ActivityProvider.providers.clear();
		Broker.brokers.clear();
	}
}
