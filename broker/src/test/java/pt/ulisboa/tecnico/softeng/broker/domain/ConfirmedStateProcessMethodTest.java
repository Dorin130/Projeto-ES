package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class ConfirmedStateProcessMethodTest {
	private static final boolean RENT_CAR = true;
	private static final String IBAN = "BK01987654321";
	private static final int AMOUNT = 300;
	private static final int AGE = 20;
	private static final String DRIVING_LICENSE = "IMT1234";
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private static final String CAR_CONFIRMATION = "CarConfirmation";
	private static final String buyerNIF = "123456789";
	private static final LocalDate arrival = new LocalDate(2016, 12, 19);
	private static final LocalDate departure = new LocalDate(2016, 12, 21);
	private Adventure adventure;
	private Client client;

	@Injectable
	private Broker broker;
	
	@Before
	public void setUp() {
		this.client = new Client(broker, IBAN, buyerNIF, DRIVING_LICENSE,AGE);
		this.adventure = new Adventure(this.broker, arrival, departure, client, AMOUNT, RENT_CAR);
		this.adventure.setState(State.CONFIRMED);
	}

	@Test
	public void successAll(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);		
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setVehicleConfirmation(CAR_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);				

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				CarInterface.getVehicleRentingData(CAR_CONFIRMATION);
				
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void successActivityAndPayment(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}
	
	@Test
	public void successActivityRoomAndPayment(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);		
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);				

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}
	
	@Test
	public void successActivityVehicleAndPayment(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final CarInterface carInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);		
		this.adventure.setVehicleConfirmation(CAR_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);				
				
				CarInterface.getVehicleRentingData(CAR_CONFIRMATION);
				
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void oneBankException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setVehicleConfirmation(CAR_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);				

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				CarInterface.getVehicleRentingData(CAR_CONFIRMATION);
				
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void maxBankException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setVehicleConfirmation(CAR_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);				

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				CarInterface.getVehicleRentingData(CAR_CONFIRMATION);
				
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
			}
		};

		for (int i = 0; i < ConfirmedState.MAX_BANK_EXCEPTIONS; i++) {
			this.adventure.process();
		}

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void maxMinusOneBankException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setVehicleConfirmation(CAR_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);				

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				CarInterface.getVehicleRentingData(CAR_CONFIRMATION);
				
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
			}
		};

		for (int i = 0; i < ConfirmedState.MAX_BANK_EXCEPTIONS - 1; i++) {
			this.adventure.process();
		}

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}
	
	@Test
	public void oneRemoteAccessExceptionStartingInActivity(@Mocked final ActivityInterface activityInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void maxRemoteAccessExceptionStartingInActivity(@Mocked final ActivityInterface activityInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		for (int i = 0; i < ConfirmedState.MAX_REMOTE_ERRORS; i++) {
			this.adventure.process();
		}

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void maxMinusOneRemoteAccessExceptionStartingInActivity(@Mocked final ActivityInterface activityInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		for (int i = 0; i < ConfirmedState.MAX_REMOTE_ERRORS - 1; i++) {
			this.adventure.process();
		}

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void oneRemoteAccessExceptionStartingInRoom(@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void maxRemoteAccessExceptionStartingInRoom(@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		for (int i = 0; i < ConfirmedState.MAX_REMOTE_ERRORS; i++) {
			this.adventure.process();
		}

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void maxMinusOneRemoteAccessExceptionStartingInRoom(@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		for (int i = 0; i < ConfirmedState.MAX_REMOTE_ERRORS - 1; i++) {
			this.adventure.process();
		}

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}
	
	@Test
	public void oneRemoteAccessExceptionStartingInVehicle(@Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setVehicleConfirmation(CAR_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				CarInterface.getVehicleRentingData(CAR_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void maxRemoteAccessExceptionStartingInVehicle(@Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setVehicleConfirmation(CAR_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				CarInterface.getVehicleRentingData(CAR_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		for (int i = 0; i < ConfirmedState.MAX_REMOTE_ERRORS; i++) {
			this.adventure.process();
		}

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void maxMinusOneRemoteAccessExceptionStartingInVehicle(@Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setVehicleConfirmation(CAR_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				CarInterface.getVehicleRentingData(CAR_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		for (int i = 0; i < ConfirmedState.MAX_REMOTE_ERRORS - 1; i++) {
			this.adventure.process();
		}

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}
	
	@Test
	public void oneRemoteAccessExceptionStartingInPayment(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setVehicleConfirmation(CAR_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);				

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				CarInterface.getVehicleRentingData(CAR_CONFIRMATION);
				
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void maxRemoteAccessExceptionStartingInPayment(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setVehicleConfirmation(CAR_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				CarInterface.getVehicleRentingData(CAR_CONFIRMATION);
				
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		for (int i = 0; i < ConfirmedState.MAX_REMOTE_ERRORS; i++) {
			this.adventure.process();
		}

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void maxMinusOneRemoteAccessExceptionStartingInPayment(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface,
			@Mocked final HotelInterface roomInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setVehicleConfirmation(CAR_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);				

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				CarInterface.getVehicleRentingData(CAR_CONFIRMATION);
				
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		for (int i = 0; i < ConfirmedState.MAX_REMOTE_ERRORS - 1; i++) {
			this.adventure.process();
		}

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void activityException(@Mocked final ActivityInterface activityInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new ActivityException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void hotelException(@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = new HotelException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}
	
	@Test
	public void vehicleException(@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface, @Mocked final CarInterface carInterface) {
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setVehicleConfirmation(CAR_CONFIRMATION);
		
		new Expectations() {
			{
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				
				CarInterface.getVehicleRentingData(CAR_CONFIRMATION);
				this.result = new CarException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

}