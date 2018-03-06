package pt.ulisboa.tecnico.softeng.tax.domain;


import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;


import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;


public class BuyerConstructorMethodTest {
	private static final String NIF_1 = "123456789";
	private static final String NIF_2 = "123456788";
	private static final String NAME_1 = "Hugo";
	private static final String NAME_2 = "Manuel";
	private static final String ADDRESS_1 = "Albufeira";
	private static final String ADDRESS_2 = "Lisboa";

	
	@Test(expected = TaxException.class)
	public void wrongNIFSize() {
		new Buyer("123",NAME_1,ADDRESS_1);
	}
	
	@Test
	public void success() {
		Buyer buyer = new Buyer(NIF_1, NAME_1, ADDRESS_1);
		Assert.assertEquals(NIF_1, buyer.getNIF());
		Assert.assertEquals(NAME_1, buyer.getName());
		Assert.assertEquals(ADDRESS_1, buyer.getAddress());

		Buyer buyer2 = new Buyer(NIF_2, NAME_2, ADDRESS_2);
		Assert.assertEquals(NIF_2, buyer2.getNIF());
		Assert.assertEquals(NAME_2, buyer2.getName());
		Assert.assertEquals(ADDRESS_2, buyer2.getAddress());
	}
	
	@Test(expected = TaxException.class)
	public void nullname() {
		new Buyer(NIF_1,null,ADDRESS_1);
	}
	
	@Test(expected = TaxException.class)
	public void emptyname() {
		new Buyer(NIF_1,"",ADDRESS_1);
	}

    @Test(expected = TaxException.class)
    public void emptyname2() {
        new Buyer(NIF_1,"       ",ADDRESS_1);
    }

	@Test(expected = TaxException.class)
	public void nulladdress() {
		new Buyer(NIF_1, NAME_1,null);
	}
	
	@Test(expected = TaxException.class)
	public void emptyaddress() {
		new Buyer(NIF_1, NAME_1,"");
	}

    @Test(expected = TaxException.class)
    public void emptyaddress2() {
        new Buyer(NIF_1, NAME_1,"       ");
    }
	
	@Test
	public void uniqueNIF() {
		new Buyer(NIF_1,NAME_1,ADDRESS_1);
		try {
			new Buyer(NIF_1,NAME_2,ADDRESS_2);
			fail();
		} catch (TaxException te) {
			Assert.assertEquals(NAME_1, IRS.getInstance().getTaxPayerByNIF(NIF_1).getName());
		}
	}
	
	@After
	public void tearDown() {
		IRS.getInstance().clearTaxPayers();
	}
}
