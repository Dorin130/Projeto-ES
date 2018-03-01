package pt.ulisboa.tecnico.softeng.tax.domain;


import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;



public class BuyerConstructorMethodTest {
	private IRS irs;
	
	@Before
	public void setUp() {
		irs = IRS.getInstance();
	}
	
	@Test(expected = TaxException.class)
	public void wrongNIFSize() {
		new Buyer("123","Maria","Lisboa");
	}
	
	
	@Test(expected = TaxException.class)
	public void nullname() {
		new Buyer("123456789",null,"Lisboa");
	}
	
	@Test(expected = TaxException.class)
	public void emptyname() {
		new Buyer("123456789","","Lisboa");
	}
	
	@Test(expected = TaxException.class)
	public void nulladdress() {
		new Buyer("123456789","Maria",null);
	}
	
	@Test(expected = TaxException.class)
	public void emptyaddress() {
		new Buyer("123456789","Maria","");
	}
	
	@Test
	public void uniqueNIF() {
		irs.addTaxPayer(new Buyer("123456789","Maria","Lisboa"));
		try {
			irs.addTaxPayer(new Buyer("123456789","Joana","Porto"));
			fail();
		} catch (TaxException te) {
			Assert.assertEquals("Maria", this.irs.getTaxPayerByNIF("123456789").getName());
		}
	}
	
	@After
	public void tearDown() {
		irs.clearTaxPayers();
	}
}
