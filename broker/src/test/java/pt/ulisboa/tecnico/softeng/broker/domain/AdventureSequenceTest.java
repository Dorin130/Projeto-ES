package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class AdventureSequenceTest {
	private static final boolean RENT_CAR = true;
	private static final boolean NOT_RENT_CAR = false;
	private static final String IBAN = "BK01987654321";
	private static final String DRIVING_LICENSE = "IMT1234";
	private static final int AMOUNT = 300;
	private static final int AGE = 20;
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String PAYMENT_CANCELLATION = "PaymentCancellation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ACTIVITY_CANCELLATION = "ActivityCancellation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private static final String ROOM_CANCELLATION = "RoomCancellation";
	//TODO
	//private static final String CAR_CONFIRMATION = "CarConfirmation";
	//private static final String CAR_CANCELLATION = "CarCancellation";
	private static final String buyerNIF = "123456789";
	private static final String sellerNIF = "987654321";
	private static final LocalDate arrival = new LocalDate(2016, 12, 19);
	private static final LocalDate departure = new LocalDate(2016, 12, 21);

	@Injectable
	private Broker broker;

	private final Client client = new Client(broker, IBAN, buyerNIF, DRIVING_LICENSE,AGE);
	@Test
	public void successSequenceOne(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface, @Mocked CarInterface carInterface) {
		new Expectations() {
			{
				ActivityInterface.reserveActivity(arrival, departure, AGE);
				this.result = ACTIVITY_CONFIRMATION;

				HotelInterface.reserveRoom(Type.SINGLE, arrival, departure);
				this.result = ROOM_CONFIRMATION;
				
				//TODO
				//CarInterface.processRenting(TODO);
				//this.result = CAR_CONFIRMATION;
				
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				//TODO
				//CarInterface.getRentingData(CAR_CONFIRMATION);
				
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, departure, client, AMOUNT, RENT_CAR);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();
		//adventure.process();

		Assert.assertEquals(State.CONFIRMED, adventure.getState());
	}
	
	@Test
	public void successSequenceTwo(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface, @Mocked CarInterface carInterface) {
		new Expectations() {
			{
				ActivityInterface.reserveActivity(arrival, departure, AGE);
				this.result = ACTIVITY_CONFIRMATION;

				HotelInterface.reserveRoom(Type.SINGLE, arrival, departure);
				this.result = ROOM_CONFIRMATION;
				
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, departure, client, AMOUNT, NOT_RENT_CAR);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CONFIRMED, adventure.getState());
	}
	
	@Test
	public void successSequenceThree(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface, @Mocked CarInterface carInterface) {
		new Expectations() {
			{
				ActivityInterface.reserveActivity(arrival, arrival, AGE);
				this.result = ACTIVITY_CONFIRMATION;

				//TODO
				//CarInterface.processRenting(TODO);
				//this.result = CAR_CONFIRMATION;
				
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, arrival, client, AMOUNT, RENT_CAR);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CONFIRMED, adventure.getState());
	}

	@Test
	public void successSequenceFour(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface, @Mocked CarInterface carInterface) {
		new Expectations() {
			{
				ActivityInterface.reserveActivity(arrival, arrival, AGE);
				this.result = ACTIVITY_CONFIRMATION;
				
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, arrival, client, AMOUNT, NOT_RENT_CAR);

		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CONFIRMED, adventure.getState());
	}

	@Test
	public void unsuccessSequenceOne(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface, @Mocked CarInterface carInterface) {
		new Expectations() {
			{
				ActivityInterface.reserveActivity(arrival, departure, AGE);
				this.result = new ActivityException();
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, departure, client, AMOUNT, NOT_RENT_CAR);

		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}

	@Test
	public void unsuccessSequenceTwo(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface, @Mocked CarInterface carInterface) {
		new Expectations() {
			{
				ActivityInterface.reserveActivity(arrival, arrival, AGE);
				this.result = ACTIVITY_CONFIRMATION;
				
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = new BankException();
				
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, arrival, client, AMOUNT, NOT_RENT_CAR);

		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}

	@Test
	public void unsuccessSequenceThree(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface, @Mocked CarInterface carInterface) {
		new Expectations() {
			{
				ActivityInterface.reserveActivity(arrival, departure, AGE);
				this.result = ACTIVITY_CONFIRMATION;
				
				HotelInterface.reserveRoom(Type.SINGLE, arrival, departure);
				this.result = ROOM_CONFIRMATION;

				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = new BankException();

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
			}
		};
		
		Adventure adventure = new Adventure(this.broker, arrival, departure, client, AMOUNT, NOT_RENT_CAR);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}
	
	@Test
	public void unsuccessSequenceFour(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface, @Mocked CarInterface carInterface) {
		new Expectations() {
			{
				ActivityInterface.reserveActivity(arrival, arrival, AGE);
				this.result = ACTIVITY_CONFIRMATION;
				
				//TODO
				//CarInterface.processRenting(TODO);
				//this.result = CAR_CONFIRMATION;

				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = new BankException();

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				
				//TODO
				//CarInterface.cancelRenting(CAR_CONFIRMATION);
				//this.result = CAR_CANCELLATION;
			}
		};
		
		Adventure adventure = new Adventure(this.broker, arrival, arrival, client, AMOUNT, RENT_CAR);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}
		
	@Test
	public void unsuccessSequenceFive(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface, @Mocked CarInterface carInterface) {
		new Expectations() {
			{
				ActivityInterface.reserveActivity(arrival, departure, AGE);
				this.result = ACTIVITY_CONFIRMATION;
			
				HotelInterface.reserveRoom(Type.SINGLE, arrival, departure);
				this.result = ROOM_CONFIRMATION;
				
				//TODO
				//CarInterface.processRenting(TODO);
				//this.result = CAR_CONFIRMATION;

				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = new BankException();

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
				
				//TODO
				//CarInterface.cancelRenting(CAR_CONFIRMATION);
				//this.result = CAR_CANCELLATION;
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, departure, client, AMOUNT, RENT_CAR);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}
		
	@Test
	public void unsuccessSequenceSix(@Mocked final BankInterface bankInterface, 
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface, @Mocked CarInterface carInterface) {
		new Expectations() {
			{
				ActivityInterface.reserveActivity(arrival, departure, AGE);
				this.result = ACTIVITY_CONFIRMATION;
		
				HotelInterface.reserveRoom(Type.SINGLE, arrival, departure);
				this.result = new HotelException();
				
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, departure, client, AMOUNT, NOT_RENT_CAR);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}
	
	@Test
	public void unsuccessSequenceSeven(@Mocked final BankInterface bankInterface, 
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface, @Mocked CarInterface carInterface) {
		new Expectations() {
			{
				ActivityInterface.reserveActivity(arrival, arrival, AGE);
				this.result = ACTIVITY_CONFIRMATION;
		
				//TODO
				//CarInterface.processRenting(TODO);
				//this.result = new Car_Exception();
				
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, arrival, client, AMOUNT, RENT_CAR);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}
	
	@Test
	public void unsuccessSequenceEight(@Mocked final BankInterface bankInterface, 
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface, @Mocked CarInterface carInterface) {
		new Expectations() {
			{
				ActivityInterface.reserveActivity(arrival, departure, AGE);
				this.result = ACTIVITY_CONFIRMATION;
		
				HotelInterface.reserveRoom(Type.SINGLE, arrival, departure);
				this.result = ROOM_CONFIRMATION;
				
				//TODO
				//CarInterface.processRenting(TODO);
				//this.result = new Car_Exception();
				
				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;
				
				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, departure, client, AMOUNT, RENT_CAR);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}

	@Test
	public void unsuccessSequenceNine(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface, @Mocked CarInterface carInterface) {
		new Expectations() {
			{	
				ActivityInterface.reserveActivity(arrival, departure, AGE);
				this.result = ACTIVITY_CONFIRMATION;				

				HotelInterface.reserveRoom(Type.SINGLE, arrival, departure);
				this.result = ROOM_CONFIRMATION;
				
				//TODO
				//CarInterface.processRenting(TODO);
				//this.result = CAR_CONFIRMATION;
				
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
				this.times = ConfirmedState.MAX_BANK_EXCEPTIONS;

				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;

				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
				
				//TODO
				//CarInterface.cancelRenting(CAR_CONFIRMATION);
				//this.result = CAR_CANCELLATION;
			}
		};

		Adventure adventure = new Adventure(this.broker, arrival, departure, client, AMOUNT, RENT_CAR);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();
		for (int i = 0; i < ConfirmedState.MAX_BANK_EXCEPTIONS; i++) {
			adventure.process();
		}
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}

}