package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class AdventureConstructorMethodTest {
	private static final int AGE = 20;
	private static final int AMOUNT = 300;
	private static final String IBAN = "BK011234567";
	private static final String buyerNIF = "123456789";
	private static final String sellerNIF = "987654321";
	private Broker broker;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);

	@Before
	public void setUp() {
		this.broker = new Broker("BR01", "eXtremeADVENTURE", buyerNIF, sellerNIF);
	}

	@Test
	public void success() {
		Client client = new Client(IBAN, AGE, buyerNIF);
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, client, AMOUNT);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(20, adventure.getAge());
		Assert.assertEquals(IBAN, adventure.getIBAN());
		Assert.assertEquals(300, adventure.getAmount());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void nullBroker() {
		Client client = new Client(IBAN, AGE, buyerNIF);
		new Adventure(null, this.begin, this.end, client, AMOUNT);
	}

	@Test(expected = BrokerException.class)
	public void nullBegin() {
		Client client = new Client(IBAN, AGE, buyerNIF);
		new Adventure(this.broker, null, this.end, client, AMOUNT);
	}

	@Test(expected = BrokerException.class)
	public void nullEnd() {
		Client client = new Client(IBAN, AGE, buyerNIF);
		new Adventure(this.broker, this.begin, null, client, AMOUNT);
	}

	@Test
	public void successEqual18() {
		Client client = new Client(IBAN, 18, buyerNIF);
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, client, AMOUNT);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(18, adventure.getAge());
		Assert.assertEquals(IBAN, adventure.getIBAN());
		Assert.assertEquals(300, adventure.getAmount());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void lessThan18Age() {
		Client client = new Client(IBAN, 17, buyerNIF);
		new Adventure(this.broker, this.begin, this.end, client, AMOUNT);
	}

	@Test
	public void successEqual100() {
		Client client = new Client(IBAN, 100, buyerNIF);
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, client, AMOUNT);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(100, adventure.getAge());
		Assert.assertEquals(IBAN, adventure.getIBAN());
		Assert.assertEquals(300, adventure.getAmount());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void over100() {
		Client client = new Client(IBAN, 101, buyerNIF);
		new Adventure(this.broker, this.begin, this.end, client, AMOUNT);
	}

	@Test(expected = BrokerException.class)
	public void nullIBAN() {
		Client client = new Client(null, AGE, buyerNIF);
		new Adventure(this.broker, this.begin, this.end, client, AMOUNT);
	}

	@Test(expected = BrokerException.class)
	public void emptyIBAN() {
		Client client = new Client("    ", AGE, buyerNIF);
		new Adventure(this.broker, this.begin, this.end, client, AMOUNT);
	}

	@Test(expected = BrokerException.class)
	public void negativeAmount() {
		Client client = new Client(IBAN, AGE, buyerNIF);
		new Adventure(this.broker, this.begin, this.end, client,  -100);
	}

	@Test
	public void success1Amount() {
		Client client = new Client(IBAN, AGE, buyerNIF);
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, client, 1);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(20, adventure.getAge());
		Assert.assertEquals(IBAN, adventure.getIBAN());
		Assert.assertEquals(1, adventure.getAmount());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void zeroAmount() {
		Client client = new Client(IBAN, AGE, buyerNIF);
		new Adventure(this.broker, this.begin, this.end, client, 0);
	}

	@Test
	public void successEqualDates() {
		Client client = new Client(IBAN, AGE, buyerNIF);
		Adventure adventure = new Adventure(this.broker, this.begin, this.begin, client, AMOUNT);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.begin, adventure.getEnd());
		Assert.assertEquals(20, adventure.getAge());
		Assert.assertEquals(IBAN, adventure.getIBAN());
		Assert.assertEquals(300, adventure.getAmount());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void inconsistentDates() {
		Client client = new Client(IBAN, AGE, buyerNIF);
		new Adventure(this.broker, this.begin, this.begin.minusDays(1), client, AMOUNT);
	}

	@After
	public void tearDown() {
		Broker.brokers.clear();
	}

}
