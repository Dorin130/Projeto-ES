package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class IRSSubmitInvoiceMethodTest {
    private IRS irs;
    private TaxPayer buyer;
    private TaxPayer seller;
    private ItemType alimentares;
    private ItemType vinho;
    private LocalDate date;

    @Before
    public void setUp() {
        this.irs = IRS.getInstance();

        this.buyer = new Buyer("12343", "Manel", "Lisboa");
        this.seller = new Seller("1337", "Hugo", "Albufeira");

        this.irs.addTaxPayer(buyer);
        this.irs.addTaxPayer(seller);

        this.alimentares = new ItemType("alimentares", 13);
        this.vinho = new ItemType("vinho", 50);

        this.irs.addItemType(alimentares);
        this.irs.addItemType(vinho);

        this.date = new LocalDate(2016, 12, 21);
    }

    @Test
    public void success() {
        irs.submitInvoice(new InvoiceData(this.seller.getNIF(), this.buyer.getNIF(), this.vinho.getName(), 10.5f, this.date));
    }

    @Test(expected = TaxException.class)
    public void dateException() {
        irs.submitInvoice(new InvoiceData(this.seller.getNIF(), this.buyer.getNIF(), this.vinho.getName(), 10.5f, new LocalDate(1969, 12, 21)));
    }

    @Test(expected = TaxException.class)
    public void nullSellerException() {
        irs.submitInvoice(new InvoiceData(null, this.buyer.getNIF(), this.vinho.getName(), 10.5f, this.date));
    }

    @Test(expected = TaxException.class)
    public void emptySellerException() {
        irs.submitInvoice(new InvoiceData("", this.buyer.getNIF(), this.vinho.getName(), 10.5f, this.date));
    }

    @Test(expected = TaxException.class)
    public void nullBuyerException() {
        irs.submitInvoice(new InvoiceData(this.seller.getNIF(), null, this.vinho.getName(), 10.5f, this.date));
    }

    @Test(expected = TaxException.class)
    public void emptyBuyerException() {
        irs.submitInvoice(new InvoiceData(this.seller.getNIF(), "", this.vinho.getName(), 10.5f, this.date));
    }


    @Test(expected = TaxException.class)
    public void nullItemTypeException() {
        irs.submitInvoice(new InvoiceData(this.seller.getNIF(), this.buyer.getNIF(), null, 10.5f, this.date));
    }

    @Test(expected = TaxException.class)
    public void emptyItemTypeException() {
        irs.submitInvoice(new InvoiceData(this.seller.getNIF(), this.buyer.getNIF(), "", 10.5f, this.date));
    }

}
