package pt.ulisboa.tecnico.softeng.broker;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure;
import pt.ulisboa.tecnico.softeng.broker.domain.Broker;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

public class Application {

	public static void main(String[] args) {
		
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		
		
		System.out.println("Adventures!");

		Bank bank = new Bank("MoneyPlus", "BK01");
		Account account = new Account(bank, new Client(bank, "José dos Anzóis"));
		account.deposit(1000);

		Broker broker = new Broker("BR01", "Fun");
		
		Hotel hotel = new Hotel("XPTO123", "Paris");
		new Room(hotel, "01", Type.SINGLE);

		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		Activity activity = new Activity(provider, "Bush Walking", 18, 80, 10);
		new ActivityOffer(activity, begin, end);
		new ActivityOffer(activity, begin, begin);

		
		Adventure adventure = new Adventure(broker, begin, end, 33, account.getIBAN(), 50);
		adventure.process();

		System.out.println("Your payment reference is " + adventure.getPaymentConfirmation() + " and you have "
				+ account.getBalance() + " euros left in your account");
		adventure.process();
		adventure.process();
		adventure.process();
		
		System.out.println("Your activity reference is " + adventure.getActivityConfirmation());
		System.out.println("Your hotel reference is " + adventure.getRoomConfirmation());

	}

}
