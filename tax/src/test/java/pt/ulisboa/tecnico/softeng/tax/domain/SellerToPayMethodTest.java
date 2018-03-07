package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class SellerToPayMethodTest {
	
	private static final int DELTA = 0;
	private static final String SELLER_NIF = "123456789";
	private static final String BUYER_NIF = "123456788";
	
	private static final String ITEM_1 = "Comida";
	private static final String ITEM_2 = "Vinho";
	private static final String ITEM_NO_TAX = "ItemNoTax";
	
	private static final int YEAR_TOO_OLD = 1950;
	
	private LocalDate dateMin = new LocalDate("1970-01-01");
	private LocalDate date = new LocalDate("2018-01-01");
	private int yearMin = dateMin.getYear();
	private int year = date.getYear() ;
	
	private IRS irs;
	private Seller seller;
	
	@Before
	public void setUp() {
		this.irs = IRS.getInstance();
		this.seller = new Seller(SELLER_NIF, "Marco", "Parchal");
		new Buyer(BUYER_NIF, "Manel", "Figueira");
		new ItemType(ITEM_1, 50);
		new ItemType(ITEM_2, 25);
		new ItemType(ITEM_NO_TAX, 0);
	}
	
	@Test
	public void successNoInvoice() {
		assertEquals(0, this.seller.toPay(this.year), DELTA);
	}
	
	@Test
	public void successOneInvoiceMinYear1970() {
		new Invoice(100, this.dateMin, ITEM_1, SELLER_NIF, BUYER_NIF);
		assertEquals(50, this.seller.toPay(this.yearMin), DELTA);
	}
	
	@Test
	public void successOneInvoiceYearBiggerThanMin() {
        new Invoice(100, this.date, ITEM_1, SELLER_NIF,  BUYER_NIF);
        Assert.assertEquals(50, seller.toPay(this.year), DELTA);
	}
	
	@Test
	public void successMultipleInvoices() {
        new Invoice(100, this.date, ITEM_1, SELLER_NIF,  BUYER_NIF);
        new Invoice(100, this.date, ITEM_2, SELLER_NIF,  BUYER_NIF);
        Assert.assertEquals(75, seller.toPay(this.year), DELTA);
	}
	
	@Test
	public void successNoInvoiceInYear() {
		new Invoice(100, this.date, ITEM_1, SELLER_NIF,  BUYER_NIF);
        Assert.assertEquals(0, seller.toPay(yearMin), DELTA);
	}
	
	@Test
	public void successNoTax() {
        new Invoice(100,this.date, ITEM_NO_TAX, SELLER_NIF,  BUYER_NIF);
        Assert.assertEquals(0, seller.toPay(year), DELTA);
	}
	
	@Test(expected = TaxException.class)
	public void lowerYear() {
		this.seller.toPay(YEAR_TOO_OLD);
	}
	
	@After
	public void tearDown() {
		this.irs.clearTaxPayers();
		this.irs.clearItemTypes();
	}
}