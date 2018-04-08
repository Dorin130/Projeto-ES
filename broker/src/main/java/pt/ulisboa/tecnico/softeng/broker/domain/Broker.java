package pt.ulisboa.tecnico.softeng.broker.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class Broker {
	private static Logger logger = LoggerFactory.getLogger(Broker.class);

	public static Set<Broker> brokers = new HashSet<>();

	private final String code;
	private final String name;
	private final String sellerNIF;
	private final String buyerNIF;
	private final Set<Adventure> adventures = new HashSet<>();
	private final Set<BulkRoomBooking> bulkBookings = new HashSet<>();

	public Broker(String code, String name, String buyerNIF, String sellerNIF) {
		checkCode(code);
		this.code = code;

		checkName(name);
		this.name = name;

		checkNIF(buyerNIF);
		this.buyerNIF = buyerNIF;

		checkNIF(sellerNIF);
		this.sellerNIF = sellerNIF;

		Broker.brokers.add(this);
	}


	private void checkNIF(String NIF) {
		if (NIF == null || NIF.length() != 9) {
			throw new BrokerException();
		}
		try {
			Integer.parseInt(NIF);
		} catch (NumberFormatException nfe) {
			throw new BrokerException();
		}

	}

	private void checkCode(String code) {
		if (code == null || code.trim().length() == 0) {
			throw new BrokerException();
		}

		for (Broker broker : Broker.brokers) {
			if (broker.getCode().equals(code)) {
				throw new BrokerException();
			}
		}
	}

	private void checkName(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new BrokerException();
		}
	}

	String getCode() {
		return this.code;
	}

	String getName() {
		return this.name;
	}

	String getBuyerNIF() {
		return this.buyerNIF;
	}

	String getSellerNIF() {
		return this.sellerNIF;
	}

	public int getNumberOfAdventures() {
		return this.adventures.size();
	}

	public void addAdventure(Adventure adventure) {
		this.adventures.add(adventure);
	}

	public boolean hasAdventure(Adventure adventure) {
		return this.adventures.contains(adventure);
	}

	public void bulkBooking(int number, LocalDate arrival, LocalDate departure, String nif, String iban) {
		BulkRoomBooking bulkBooking = new BulkRoomBooking(number, arrival, departure, nif, iban);
		this.bulkBookings.add(bulkBooking);
		bulkBooking.processBooking();
	}

}
