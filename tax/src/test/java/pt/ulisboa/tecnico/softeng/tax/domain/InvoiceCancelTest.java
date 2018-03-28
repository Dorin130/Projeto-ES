package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InvoiceCancelTest {
	private static final String SELLER_NIF = "123456789";
	private static final String BUYER_NIF = "987654321";
	private static final String FOOD = "FOOD";
	private static final int VALUE = 16;
	private static final int TAX = 23;
	private final LocalDate date = new LocalDate(2018, 02, 13);

	private Seller seller;
	private Buyer buyer;
	private ItemType itemType;

	@Before
	public void setUp() {
		IRS irs = IRS.getIRS();
		this.seller = new Seller(irs, SELLER_NIF, "José Vendido", "Somewhere");
		this.buyer = new Buyer(irs, BUYER_NIF, "Manuel Comprado", "Anywhere");
		this.itemType = new ItemType(irs, FOOD, TAX);
	}

	@Test
	public void success() {
		Invoice invoice = new Invoice(VALUE, this.date, this.itemType, this.seller, this.buyer);
		String cancel = invoice.cancel();
		
		assertNotNull(invoice.getReference());
		assertEquals(VALUE, invoice.getValue(), 0.0f);
		assertEquals(this.date, invoice.getDate());
		assertEquals(this.itemType, invoice.getItemType());
		assertEquals(this.seller, invoice.getSeller());
		assertEquals(this.buyer, invoice.getBuyer());
		assertEquals(VALUE * TAX / 100.0, invoice.getIva(), 0.00001f);

		assertEquals(invoice, this.seller.getInvoiceByReference(invoice.getReference()));
		assertEquals(invoice, this.buyer.getInvoiceByReference(invoice.getReference()));
		
		assertTrue(invoice.isCancelled());
		assertNotNull(invoice.getCancellationDate());
		assertEquals(cancel, invoice.getCancellation());
		assertEquals(invoice, this.seller.getInvoiceByReference(cancel));
		assertEquals(invoice, this.buyer.getInvoiceByReference(cancel));
	}

	@After
	public void tearDown() {
		IRS.getIRS().clearAll();
	}

}
