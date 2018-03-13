package pt.ulisboa.tecnico.softeng.tax.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

import org.joda.time.LocalDate;

public class InvoiceConstructorMethodTest {
    private static final float VALUE = 10.5f;
    private static final LocalDate DATE = new LocalDate(2010, 10, 1);
    private static final String ITEM_TYPE = "vinho";
    private static final String SELLER = "123456789";
    private static final String BUYER = "456123789";

    private IRS irs;


    @Before
    public void setUp() {
        irs = IRS.getInstance();

        new ItemType(ITEM_TYPE, 50);
        new Buyer(BUYER, "Marco", "Faro");
        new Seller(SELLER, "Nadia", "Lisboa");
    }

    @Test
    public void success() {
        Invoice invoice = new Invoice(VALUE, DATE, ITEM_TYPE, SELLER, BUYER);

        Assert.assertEquals(VALUE, invoice.getValue(), 0);
        Assert.assertEquals(DATE, invoice.getDate());
        Assert.assertEquals(ITEM_TYPE, invoice.getItemTypeName());
        Assert.assertEquals(SELLER, invoice.getSellerNIF());
        Assert.assertEquals(BUYER, invoice.getBuyerNIF());
    }
    @Test(expected = TaxException.class)
    public void nullItemType() {
        new Invoice(VALUE, DATE, null, SELLER, BUYER);
    }
    @Test(expected = TaxException.class)
    public void badSellerFormat() {
        new Invoice(VALUE, DATE, ITEM_TYPE, "123ABC123", BUYER);
    }
    @Test(expected = TaxException.class)
    public void badBuyerFormat() {
        new Invoice(VALUE, DATE, ITEM_TYPE, SELLER, "123GFD123");
    }

    @Test(expected = TaxException.class)
    public void nullSellerNIF() {
        new Invoice(VALUE, DATE, ITEM_TYPE, null, BUYER);
    }

    @Test(expected = TaxException.class)
    public void nullIBuyerNIF() {
        new Invoice(VALUE, DATE, ITEM_TYPE, SELLER, null);
    }

    @Test(expected = TaxException.class)
    public void emptyItemType() {
        new Invoice(VALUE, DATE, "", SELLER, BUYER);
    }

    @Test(expected = TaxException.class)
    public void emptySellerNIF() {
        new Invoice(VALUE, DATE, ITEM_TYPE, "", BUYER);
    }

    @Test(expected = TaxException.class)
    public void emptyIBuyerNIF() {
        new Invoice(VALUE, DATE, ITEM_TYPE, SELLER, "");
    }

    @Test(expected = TaxException.class)
    public void invalidDate() {
        new Invoice(VALUE, new LocalDate(1950, 10, 10), ITEM_TYPE, SELLER, "");
    }

    @Test(expected = TaxException.class)
    public void invalidValueZero() {
        new Invoice(0, DATE, ITEM_TYPE, SELLER, "");
    }

    @Test(expected = TaxException.class)
    public void invalidValueNegative() {
        new Invoice(-12f, DATE, ITEM_TYPE, SELLER, "");
    }

    @After
    public void tearDown() {
        this.irs.clearItemTypes();
        this.irs.clearTaxPayers();
    }

}
