package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class UndoState extends AdventureState {

	@Override
	public State getState() {
		return State.UNDO;
	}

	@Override
	public void process(Adventure adventure) {

		if (requiresCancelActivity(adventure)) {
			try {
				adventure.setActivityCancellation(
						ActivityInterface.cancelReservation(adventure.getActivityConfirmation()));
			} catch (ActivityException | RemoteAccessException ex) {
				// does not change state
			}
		}

		if (requiresCancelRoom(adventure)) {
			try {
				adventure.setRoomCancellation(HotelInterface.cancelBooking(adventure.getRoomConfirmation()));
			} catch (HotelException | RemoteAccessException ex) {
				// does not change state
			}
		}
		
		if (requiresCancelVehicle(adventure)) {
			try {
				adventure.setVehicleCancellation(CarInterface.cancelVehicleRenting(adventure.getVehicleConfirmation()));
			} catch (CarException | RemoteAccessException ex) {
				// does not change state
			}
		}
		
		if (requiresCancelPayment(adventure)) {
			try {
				adventure.setPaymentCancellation(BankInterface.cancelPayment(adventure.getPaymentConfirmation()));
			} catch (BankException | RemoteAccessException ex) {
				// does not change state
			}
		}
		
		if (requiresCancelInvoice(adventure)) {
			try {
				TaxInterface.cancelInvoice(adventure.getTaxConfirmation());
				adventure.setCancelledInvoice(true);
			} catch (TaxException | RemoteAccessException ex) {
				// does not change state
			}
		}

		if (!requiresCancelPayment(adventure) && !requiresCancelActivity(adventure) &&
				!requiresCancelRoom(adventure) && !requiresCancelVehicle(adventure) && !requiresCancelInvoice(adventure)) {
			adventure.setState(State.CANCELLED);
		}
	}
	

	public boolean requiresCancelActivity(Adventure adventure) {
		return adventure.getActivityConfirmation() != null && adventure.getActivityCancellation() == null;
	}
	
	public boolean requiresCancelRoom(Adventure adventure) {
		return adventure.getRoomConfirmation() != null && adventure.getRoomCancellation() == null;
	}
	
	public boolean requiresCancelVehicle(Adventure adventure) {
		return adventure.getVehicleConfirmation() != null && adventure.getVehicleCancellation() == null;
	}

	public boolean requiresCancelPayment(Adventure adventure) {
		return adventure.getPaymentConfirmation() != null && adventure.getPaymentCancellation() == null;
	}
	
	public boolean requiresCancelInvoice(Adventure adventure) {
		return adventure.getTaxConfirmation() != null && !adventure.getTaxCancellation();
	}

}
