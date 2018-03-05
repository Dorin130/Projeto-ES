package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;


public class IRSGetItemTypeByNameMethodTest {
	private IRS irs;
	private ItemType alimentares;
	private ItemType vinho;
	@Before
	public void setUp() {
		this.irs = IRS.getInstance();
		this.alimentares = new ItemType("alimentares", 13);
		this.vinho = new ItemType("vinho", 50);
		
		this.irs.addItemType(alimentares);
		this.irs.addItemType(vinho);
	}
	
	@Test
	public void success() {
		ItemType ret = this.irs.getItemTypeByName("vinho");
		assertEquals(ret, this.vinho);
	}
	
	@Test(expected = TaxException.class)
	public void nullItemType() {
		this.irs.getItemTypeByName(null);
	}

	@Test(expected = TaxException.class)
	public void emptyTaxException() {
		this.irs.getItemTypeByName("");
	}

	@Test(expected = TaxException.class)
	public void blankTaxException() {
		this.irs.getItemTypeByName("    ");
	}

	@Test(expected = TaxException.class)
	public void emptySetOfItemType() {
		this.irs.getItemTypeByName("XPTO");
	}

	
	@After
	public void tearDown() {
		this.irs.clearItemTypes();
	}
	
}