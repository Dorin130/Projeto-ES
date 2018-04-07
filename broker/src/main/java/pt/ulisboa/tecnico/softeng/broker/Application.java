package pt.ulisboa.tecnico.softeng.broker;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure;
import pt.ulisboa.tecnico.softeng.broker.domain.Broker;
import pt.ulisboa.tecnico.softeng.broker.domain.Client;

public class Application {

	public static void main(String[] args) {
		System.out.println("Adventures!");

		Bank bank = new Bank("MoneyPlus", "BK01");

		Client client = new Client("BK011", 30, "987654321");

		Account account = new Account(bank, new pt.ulisboa.tecnico.softeng.bank.domain.Client(bank,"António"));
		account.deposit(1000);

		Broker broker = new Broker("BR01", "Fun", "123456789" ,"987654321");
		Adventure adventure = new Adventure(broker, new LocalDate(), new LocalDate(), client, 50);

		adventure.process();

		System.out.println("Your payment reference is " + adventure.getPaymentConfirmation() + " and you have "
				+ account.getBalance() + " euros left in your account");
	}

}
