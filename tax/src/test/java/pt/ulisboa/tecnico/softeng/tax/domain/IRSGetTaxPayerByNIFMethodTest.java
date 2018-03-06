package pt.ulisboa.tecnico.softeng.tax.domain;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

import static org.junit.Assert.assertEquals;

public class IRSGetTaxPayerByNIFMethodTest {
    private IRS irs;
    private TaxPayer buyer;
    private TaxPayer seller;
    @Before
    public void setUp() {
        this.irs = IRS.getInstance();
        this.buyer = new Buyer("123456789", "Manel", "Lisboa");
        this.seller = new Seller("123456456", "Hugo", "Albufeira");

        this.irs.addTaxPayer(buyer);
        this.irs.addTaxPayer(seller);
    }

    @Test
    public void success() {
        TaxPayer ret = this.irs.getTaxPayerByNIF("123456456");
        assertEquals(ret, this.seller);
    }

    @Test(expected = TaxException.class)
    public void nullItemType() {
        this.irs.getTaxPayerByNIF(null);
    }

    @Test(expected = TaxException.class)
    public void emptyTaxException() {
        this.irs.getTaxPayerByNIF("");
    }

    @Test(expected = TaxException.class)
    public void blankTaxException() {
        this.irs.getTaxPayerByNIF("    ");
    }

    @Test(expected = TaxException.class)
    public void badNIFTaxException() {
        this.irs.getTaxPayerByNIF("A12345678");
    }
    @After
    public void tearDown() {
        this.irs.clearTaxPayers();
    }

}
