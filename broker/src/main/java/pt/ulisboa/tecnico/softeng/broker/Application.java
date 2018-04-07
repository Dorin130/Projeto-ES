package pt.ulisboa.tecnico.softeng.broker;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure;
import pt.ulisboa.tecnico.softeng.broker.domain.Broker;
import pt.ulisboa.tecnico.softeng.broker.domain.Client;

public class Application {
	private static final int AGE = 20;
	private static final String IBAN = "1234v";
	private static final String NIF = "123456789";
	private static final String DRIVING_LICENSE = "IMT1234";

	public static void main(String[] args) {
		System.out.println("Adventures!");

		Bank bank = new Bank("MoneyPlus", "BK01");

		Account account = new Account(bank, new pt.ulisboa.tecnico.softeng.bank.domain.Client(bank,"António"));
		account.deposit(1000);

		Broker broker = new Broker("BR01", "Fun", "123456789" ,"987654321");

		Client client = new Client(broker,"BK011", "987654321",DRIVING_LICENSE, 30 );
		Adventure adventure = new Adventure(broker, new LocalDate(), new LocalDate(), client, 50, true);

		adventure.process();

		System.out.println("Your payment reference is " + adventure.getPaymentConfirmation() + " and you have "
				+ account.getBalance() + " euros left in your account");
	}

}
