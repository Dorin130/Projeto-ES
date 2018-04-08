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
	private static final String DRIVING_LICENSE = "IMT1234";
	private static final String buyerNIF = "123456789";
	private static final String sellerNIF = "987654321";
	private Broker broker;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private static final boolean WANTSCAR = true;

	@Before
	public void setUp() {
		this.broker = new Broker("BR01", "eXtremeADVENTURE", buyerNIF, sellerNIF);
	}

	@Test
	public void success() {

		Client client = new Client(this.broker, IBAN, buyerNIF,DRIVING_LICENSE, AGE);
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, client, AMOUNT, true);

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
		Client client = new Client(this.broker, IBAN, buyerNIF,DRIVING_LICENSE, AGE);
		new Adventure(null, this.begin, this.end, client, AMOUNT, WANTSCAR);
	}

	@Test(expected = BrokerException.class)
	public void nullBegin() {
		Client client = new Client(this.broker, IBAN, buyerNIF,DRIVING_LICENSE, AGE);
		new Adventure(this.broker, null, this.end, client, AMOUNT, WANTSCAR);
	}

	@Test(expected = BrokerException.class)
	public void nullEnd() {
		Client client = new Client(this.broker, IBAN, buyerNIF,DRIVING_LICENSE, AGE);
		new Adventure(this.broker, this.begin, null, client, AMOUNT, WANTSCAR);
	}

	@Test
	public void successEqual18() {
		Client client = new Client(this.broker, IBAN, buyerNIF,DRIVING_LICENSE, 18);
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, client, AMOUNT, WANTSCAR);

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
		Client client = new Client(this.broker, IBAN, buyerNIF,DRIVING_LICENSE, 17);
		new Adventure(this.broker, this.begin, this.end, client, AMOUNT, WANTSCAR);
	}

	@Test
	public void successEqual100() {
		Client client = new Client(this.broker, IBAN, buyerNIF,DRIVING_LICENSE, 100);
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, client, AMOUNT, WANTSCAR);

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
		Client client = new Client(this.broker, IBAN, buyerNIF,DRIVING_LICENSE, 101);
		new Adventure(this.broker, this.begin, this.end, client, AMOUNT,WANTSCAR);
	}

	@Test(expected = BrokerException.class)
	public void nullIBAN() {
		Client client = new Client(this.broker, null, buyerNIF,DRIVING_LICENSE, AGE);
		new Adventure(this.broker, this.begin, this.end, client, AMOUNT,WANTSCAR);
	}

	@Test(expected = BrokerException.class)
	public void emptyIBAN() {
		Client client = new Client(this.broker, "    ", buyerNIF,DRIVING_LICENSE, AGE);
		new Adventure(this.broker, this.begin, this.end, client, AMOUNT,WANTSCAR);
	}

	@Test(expected = BrokerException.class)
	public void negativeAmount() {
		Client client = new Client(this.broker, IBAN, buyerNIF,DRIVING_LICENSE, AGE);
		new Adventure(this.broker, this.begin, this.end, client,  -100,WANTSCAR);
	}

	@Test
	public void success1Amount() {
		Client client = new Client(this.broker, IBAN, buyerNIF,DRIVING_LICENSE, AGE);
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, client, 1,WANTSCAR);

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
		Client client = new Client(this.broker, IBAN, buyerNIF,DRIVING_LICENSE, AGE);
		new Adventure(this.broker, this.begin, this.end, client, 0,WANTSCAR);
	}

	@Test
	public void successEqualDates() {
		Client client = new Client(this.broker, IBAN, buyerNIF,DRIVING_LICENSE, AGE);
		Adventure adventure = new Adventure(this.broker, this.begin, this.begin, client, AMOUNT,WANTSCAR);

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
		Client client = new Client(this.broker, IBAN, buyerNIF,DRIVING_LICENSE, AGE);
		new Adventure(this.broker, this.begin, this.begin.minusDays(1), client, AMOUNT,WANTSCAR);
	}

	@After
	public void tearDown() {
		Broker.brokers.clear();
	}

}
