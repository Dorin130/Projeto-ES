package pt.ulisboa.tecnico.softeng.tax.domain;


import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;


public class BuyerTaxReturnMethodTest {
    IRS irs;
    LocalDate localDate =  new LocalDate("2018-01-01");

    double delta = 0;
    @Before
    public void setUp() {
        irs = IRS.getInstance();
        irs.clearTaxPayers();
        irs.clearItemTypes();
        new ItemType("comida",1);
        new ItemType("bebida",0.5);
        new ItemType("void",0);
    }
    @Test
    public void taxReturnWithNoInvoice() {
        Buyer buyer = new Buyer("123456789", "Hugo", "Albufeira");
        Assert.assertEquals(0, buyer.taxReturn(this.localDate.getYear()), delta);

    }

    @Test
    public void taxReturnWithSingleInvoice() {
        Buyer buyer = new Buyer("123456789", "Hugo", "Albufeira");
        Seller seller = new Seller("123456788", "Manel", "Lisboa");
        Invoice invoice = new Invoice(100, this.localDate, "comida", "123456788",  "123456789");
        seller.addInvoice(invoice);
        buyer.addInvoice(invoice);
        Assert.assertEquals(5, buyer.taxReturn(this.localDate.getYear()), delta);

    }

    @Test
    public void taxReturnWithMultipleInvoice() {
        Buyer buyer = new Buyer("123456789", "Hugo", "Albufeira");
        Seller seller = new Seller("123456788", "Manel", "Lisboa");
        Invoice invoice = new Invoice(100, this.localDate, "comida", "123456788",  "123456789");
        Invoice invoice2 = new Invoice(100, this.localDate, "bebida", "123456788",  "123456789");
        seller.addInvoice(invoice);
        seller.addInvoice(invoice2);
        buyer.addInvoice(invoice);
        buyer.addInvoice(invoice2);
        Assert.assertEquals(7.5, buyer.taxReturn(this.localDate.getYear()), delta);
    }

    @Test(expected = TaxException.class)
    public void lowYear() {
        Buyer buyer = new Buyer("123456789", "Hugo", "Albufeira");
        buyer.taxReturn(1900);
    }

    @Test
    public void noTaxApplied() {
        Buyer buyer = new Buyer("123456789", "Hugo", "Albufeira");
        Seller seller = new Seller("123456788", "Manel", "Lisboa");
        Invoice invoice = new Invoice(100,this.localDate, "void", "123456788", "123456789");
        seller.addInvoice(invoice);
        buyer.addInvoice(invoice);
        Assert.assertEquals(0, buyer.taxReturn(this.localDate.getYear()),delta);

    }

    @Test
    //NOTE: There was an invoice but the years don't match
    public void noInvoicesInTheYear() {
        Buyer buyer = new Buyer("123456789", "Hugo", "Albufeira");
        Seller seller = new Seller("123456788", "Manel", "Lisboa");
        Invoice invoice = new Invoice(100, this.localDate, "comida", "123456788",  "123456789");
        seller.addInvoice(invoice);
        buyer.addInvoice(invoice);
        Assert.assertEquals(0, buyer.taxReturn(2017),delta);

    }

    @After
    public void tearDown() {
        this.irs.clearTaxPayers();
    }

}
