package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class SellerGetInvoiceByReferenceMethodTest {
	
	private static final float VALUE = 10.5f;
    private static final LocalDate DATE = new LocalDate(2010, 10, 1);
    private static final String ITEM_TYPE = "vinho";
    private static final String SELLER = "123456789";
    private static final String BUYER = "456123789";
	
	//invoice_reference from count in Invoice, so it's value is 1
	private static final String INVOICE_REFERENCE = "1";
	private IRS irs;
	private Seller seller;
	
	@Before
	public void setUp() {
		this.irs = IRS.getInstance();
		this.seller = new Seller("123456789", "Marco", "Parchal");
		new ItemType(ITEM_TYPE, 50);
	}
	
	@Test
	public void success() {
		Invoice invoice = new Invoice(VALUE, DATE, ITEM_TYPE, SELLER, BUYER);
		this.seller.addInvoice(invoice);
		
		Invoice invoiceGet = this.seller.getInvoiceByReference(invoice.getReference());
		Assert.assertEquals(invoice.getReference(), invoiceGet.getReference());
	}
	
	@Test
	public void ReturnNull() {
		Invoice invoice = this.seller.getInvoiceByReference("2");
		Assert.assertNull(invoice);
	}
	
	@Test(expected = TaxException.class)
	public void nullInvoiceReference() {
		seller.getInvoiceByReference(null);
	}
	
	@Test(expected = TaxException.class)
	public void emptyInvoiceReference() {
		seller.getInvoiceByReference("");
	}
	
	@Test(expected = TaxException.class)
	public void blankReference() {
		seller.getInvoiceByReference("   ");
	}
		
	@After
	public void tearDown() {
		this.irs.clearTaxPayers();
		this.irs.clearItemTypes();
	}
}