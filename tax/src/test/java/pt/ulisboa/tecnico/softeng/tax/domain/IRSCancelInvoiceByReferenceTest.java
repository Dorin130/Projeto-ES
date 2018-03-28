package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.fail;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mockit.Expectations;
import mockit.integration.junit4.JMockit;
import org.junit.runner.RunWith;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

@RunWith(JMockit.class)
public class IRSCancelInvoiceByReferenceTest {
	private static final String SELLER_NIF = "123456789";
	private static final String BUYER_NIF = "987654321";
	private static final String FOOD = "FOOD";
	private static final int VALUE = 16;
	private static final int TAX = 23;
	private final LocalDate date = new LocalDate(2018, 02, 13);

	private IRS irs;
	private Seller seller;
	private Buyer buyer;
	private ItemType itemType;
	private Invoice invoice;
	private String invoiceReference;

	@Before
	public void setUp() {
		this.irs = IRS.getIRS();
		this.seller = new Seller(this.irs, SELLER_NIF, "José Vendido", "Somewhere");
		this.buyer = new Buyer(this.irs, BUYER_NIF, "Manuel Comprado", "Anywhere");
		this.itemType = new ItemType(this.irs, FOOD, TAX);
		this.invoice = new Invoice(VALUE, this.date, this.itemType, this.seller, this.buyer);
		this.invoiceReference = this.invoice.getReference();
	}

	@Test
	public void success() {
		new Expectations() {
			{
				irs.getInvoiceByReference(invoiceReference);
				result = invoice;
				times = 1;
				
				invoice.cancel();
				times = 1;
			}
		};

		IRS.cancelInvoice(this.invoiceReference);
	}

	@Test(expected = TaxException.class)
	public void nullReference() {
		IRS.cancelInvoice(null);
	}

	@Test(expected = TaxException.class)
	public void emptyReference() {
		IRS.cancelInvoice("");
	}

	@Test
	public void doesNotExist() {
		try {
			IRS.cancelInvoice("random");
		} catch (NullPointerException npe) {
			fail();
		}
	}

	@After
	public void tearDown() {
		IRS.getIRS().clearAll();
	}

}
