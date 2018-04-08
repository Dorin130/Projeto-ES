package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class ConfirmedState extends AdventureState {
	public static int MAX_REMOTE_ERRORS = 20;
	public static int MAX_BANK_EXCEPTIONS = 5;

	private int numberOfBankExceptions = 0;

	@Override
	public State getState() {
		return State.CONFIRMED;
	}

	@Override
	public void process(Adventure adventure) {
		try {
			ActivityReservationData activityReservationData = ActivityInterface.getActivityReservationData(adventure.getActivityConfirmation());
			if (activityReservationData.getPaymentReference() == null || activityReservationData.getInvoiceReference() == null) {
				adventure.setState(State.UNDO);
			}
		} catch (ActivityException ae) {
			adventure.setState(State.UNDO);
			return;
		} catch (RemoteAccessException rae) {
			incNumOfRemoteErrors();
			if (getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
				adventure.setState(State.UNDO);
			}
			return;
		}
		resetNumOfRemoteErrors();
		
		if (adventure.getRoomConfirmation() != null) {
			try {
				RoomBookingData roomBookingData = HotelInterface.getRoomBookingData(adventure.getRoomConfirmation());
				if (roomBookingData.getPaymentReference() == null || roomBookingData.getInvoiceReference() == null) {
					adventure.setState(State.UNDO);
				}
			} catch (HotelException he) {
				adventure.setState(State.UNDO);
				return;
			} catch (RemoteAccessException rae) {
				incNumOfRemoteErrors();
				if (getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
					adventure.setState(State.UNDO);
				}
				return;
			}
			resetNumOfRemoteErrors();
		}
		
		if (adventure.getVehicleConfirmation() != null) {
			try {
				RentingData rentingData = CarInterface.getVehicleRentingData(adventure.getVehicleConfirmation());
				if (rentingData.getPaymentReference() == null || rentingData.getInvoiceReference() == null) {
					adventure.setState(State.UNDO);
				}
			} catch (CarException he) {
				adventure.setState(State.UNDO);
				return;
			} catch (RemoteAccessException rae) {
				incNumOfRemoteErrors();
				if (getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
					adventure.setState(State.UNDO);
				}
				return;
			}
			resetNumOfRemoteErrors();
		}
		
		try {
			BankInterface.getOperationData(adventure.getPaymentConfirmation());
		} catch (BankException be) {
			this.numberOfBankExceptions++;
			if (this.numberOfBankExceptions == MAX_BANK_EXCEPTIONS) {
				adventure.setState(State.UNDO);
			}
			return;
		} catch (RemoteAccessException rae) {
			incNumOfRemoteErrors();
			if (getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
				adventure.setState(State.UNDO);
			}
			return;
		}
		resetNumOfRemoteErrors();
		this.numberOfBankExceptions = 0;

		// TODO: prints the complete Adventure file, the info in operation,
		// reservation and booking

	}

}
