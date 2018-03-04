package pt.ulisboa.tecnico.softeng.tax.domain;


import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;


import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;


public class SellerConstructorTest {
	private static final String NIF_1 = "123456789";
	private static final String NIF_2 = "123456788";
	private static final String SMALLER_NIF = "12345";
	private static final String BIGGER_NIF = "12345678987654321";
	private static final String NAME_1 = "Marco";
	private static final String NAME_2 = "Manuel";
	private static final String ADDRESS_1 = "Parchal";
	private static final String ADDRESS_2 = "Figueira";
	
	@Test
	public void success() {
		Seller seller1 = new Seller(NIF_1, NAME_1, ADDRESS_1);
		Assert.assertEquals(NIF_1, seller1.getNIF());
		Assert.assertEquals(NAME_1, seller1.getName());
		Assert.assertEquals(ADDRESS_1, seller1.getAddress());
		
		Seller seller2 = new Seller(NIF_2, NAME_2, ADDRESS_2);
		Assert.assertEquals(NIF_2, seller2.getNIF());
		Assert.assertEquals(NAME_2, seller2.getName());
		Assert.assertEquals(ADDRESS_2, seller2.getAddress());
	}
	
	@Test(expected = TaxException.class)
	public void smallerWrongNIFSize() {
		new Seller(SMALLER_NIF,NAME_1,ADDRESS_1);
	}
	
	@Test(expected = TaxException.class)
	public void biggerWrongNIFSize() {
		new Seller(BIGGER_NIF, NAME_1, ADDRESS_1);
	}
	
	@Test(expected = TaxException.class)
	public void nullname() {
		new Seller(NIF_1,null,ADDRESS_1);
	}
	
	@Test(expected = TaxException.class)
	public void emptyname() {
		new Seller(NIF_1,"",ADDRESS_1);
	}

	@Test(expected = TaxException.class)
	public void nulladdress() {
		new Seller(NIF_1, NAME_1,null);
	}
	
	@Test(expected = TaxException.class)
	public void emptyaddress() {
		new Seller(NIF_1, NAME_1,"");
	}
	
	@Test
	public void nonUniqueNIF() {
		new Seller(NIF_1,NAME_1,ADDRESS_1);
		try {
			new Seller(NIF_1,NAME_2,ADDRESS_2);
			fail();
		}
		catch (TaxException te) {
			Assert.assertEquals(NAME_1, IRS.getInstance().getTaxPayerByNIF(NIF_1).getName());
		}
	}
	
	@After
	public void tearDown() {
		IRS.getInstance().clearTaxPayers();
	}
}
