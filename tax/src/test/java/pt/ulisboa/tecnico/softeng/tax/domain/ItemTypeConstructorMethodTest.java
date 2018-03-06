package pt.ulisboa.tecnico.softeng.tax.domain;

import org.junit.Test;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class ItemTypeConstructorMethodTest {
    private static final int TAX = 10;
    private static final String BATATA = "Batata";

    @Test
    public void success() {
        new ItemType(BATATA, TAX);
    }

    @Test(expected = TaxException.class)
    public void negativeTax() {
        new ItemType(BATATA, -1);
    }

    @Test(expected = TaxException.class)
    public void nullName() {
        new ItemType(null, TAX);
    }

    @Test(expected = TaxException.class)
    public void emptyName() {
        new ItemType("", TAX);
    }


}
