package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class ProcessPaymentState extends AdventureState {
	public static final int MAX_REMOTE_ERRORS = 3;

	@Override
	public State getState() {
		return State.PROCESS_PAYMENT;
	}

	@Override
	public void process(Adventure adventure) {
		try {
			adventure.setPaymentConfirmation(BankInterface.processPayment(adventure.getIBAN(), adventure.getAmount()));
			String sellerNIF = adventure.getBroker().getSellerNIF();
			String buyerNIF = adventure.getBroker().getBuyerNIF();
			adventure.setTaxConfirmation(TaxInterface.submitInvoice(sellerNIF, buyerNIF, "ADVENTURE", adventure.getAmount(), new LocalDate()));
		} catch (BankException be) {
			adventure.setState(State.UNDO);
			return;
		} catch (TaxException be) {
			adventure.setState(State.UNDO);
			return;
		} catch (RemoteAccessException rae) {
			incNumOfRemoteErrors();
			if (getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
				adventure.setState(State.UNDO);
			}
			return;
		}

		adventure.setState(State.CONFIRMED);
	}

}
