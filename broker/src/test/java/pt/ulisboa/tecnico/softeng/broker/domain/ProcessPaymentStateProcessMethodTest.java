package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

@RunWith(JMockit.class)
public class ProcessPaymentStateProcessMethodTest {
	private static final String DRIVING_LICENSE = "IMT1234";
	private static final String IBAN = "BK01987654321";
	private static final String BUYER = "123456789";
	private static final String SELLER = "987654321";
	private static final String ITEMTYPE = "ADVENTURE";
	private static final int AMOUNT = 300;
	private static final boolean NOT_RENT_CAR = false;
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String TAX_CONFIRMATION = "TaxConfirmation";
	private static final String IRS_REFERENCE = "123";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Adventure adventure;

	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
		this.adventure = new Adventure(this.broker, this.begin, this.end,
				new Client(this.broker, IBAN, BUYER, DRIVING_LICENSE,30), AMOUNT, NOT_RENT_CAR);
		this.adventure.setState(State.PROCESS_PAYMENT);
	}

	@Test
	public void success(@Mocked final BankInterface bankInterface, @Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				TaxInterface.submitInvoice(null, null, ITEMTYPE, AMOUNT, new LocalDate());
				this.result =  TAX_CONFIRMATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void bankException(@Mocked final BankInterface bankInterface, @Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = new BankException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void taxException(@Mocked final BankInterface bankInterface, @Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				TaxInterface.submitInvoice(null, null, ITEMTYPE, AMOUNT, new LocalDate());
				this.result =  new TaxException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void singleBankRemoteAccessException(@Mocked final BankInterface bankInterface, @Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.PROCESS_PAYMENT, this.adventure.getState());
	}

	@Test
	public void singleTaxRemoteAccessException(@Mocked final BankInterface bankInterface, @Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				TaxInterface.submitInvoice(null, null, ITEMTYPE, AMOUNT, new LocalDate());
				this.result =  new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void maxBankRemoteAccessException(@Mocked final BankInterface bankInterface, @Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}


	@Test
	public void maxTaxRemoteAccessException(@Mocked final BankInterface bankInterface, @Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				TaxInterface.submitInvoice(null, null, ITEMTYPE, AMOUNT, new LocalDate());
				this.result =  new RemoteAccessException();
			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void maxMinusBankOneRemoteAccessException(@Mocked final BankInterface bankInterface, @Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.PROCESS_PAYMENT, this.adventure.getState());
	}

	@Test
	public void maxMinusTaxOneRemoteAccessException(@Mocked final BankInterface bankInterface, @Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				TaxInterface.submitInvoice(null, null, ITEMTYPE, AMOUNT, new LocalDate());
				this.result =  new RemoteAccessException();
			}
		};

		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.PROCESS_PAYMENT, this.adventure.getState());
	}

	@Test
	public void twoBankRemoteAccessExceptionOneSuccess(@Mocked final BankInterface bankInterface, @Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 2) {
							this.i++;
							throw new RemoteAccessException();
						} else {
							return PAYMENT_CONFIRMATION;
						}
					}
				};
				this.times = 3;

				TaxInterface.submitInvoice(null, null, ITEMTYPE, AMOUNT, new LocalDate());
				this.result =  TAX_CONFIRMATION;

			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void twoTaxRemoteAccessExceptionOneSuccess(@Mocked final BankInterface bankInterface, @Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				TaxInterface.submitInvoice(null, null, ITEMTYPE, AMOUNT, new LocalDate());
				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 2) {
							this.i++;
							throw new RemoteAccessException();
						} else {
							return TAX_CONFIRMATION;
						}
					}
				};
				this.times = 3;

			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void oneRemoteAccessExceptionOneBankException(@Mocked final BankInterface bankInterface, @Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);

				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 1) {
							this.i++;
							throw new RemoteAccessException();
						} else {
							throw new BankException();
						}
					}
				};
				this.times = 2;

			}
		};

		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void oneRemoteAccessExceptionOneTaxException(@Mocked final BankInterface bankInterface, @Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = PAYMENT_CONFIRMATION;

				TaxInterface.submitInvoice(null, null, ITEMTYPE, AMOUNT, new LocalDate());
				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 1) {
							this.i++;
							throw new RemoteAccessException();
						} else {
							throw new TaxException();
						}
					}
				};
				this.times = 2;

			}
		};

		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

}