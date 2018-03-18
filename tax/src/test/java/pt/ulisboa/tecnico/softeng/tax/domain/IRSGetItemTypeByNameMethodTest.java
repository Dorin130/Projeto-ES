package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;


public class IRSGetItemTypeByNameMethodTest {
	private IRS irs;
	private ItemType vinho;
	@Before
	public void setUp() {
		this.irs = IRS.getInstance();
		new ItemType("alimentares", 13);
		this.vinho = new ItemType("vinho", 50);

	}

	@Test
	public void success() {
		ItemType ret = this.irs.getItemTypeByName("vinho");
		assertEquals(this.vinho, ret);
	}
	@Test
	public void moreThanOneType( ) {
		this.irs.clearItemTypes();
		new ItemType("limpeza", 12);
		new ItemType("alimentares", 13);
		this.irs.getItemTypeByName("alimentares");
	}
	
	@Test
	public void noItemType( ) {
		this.irs.clearItemTypes();
		assertEquals(null, this.irs.getItemTypeByName("alimentares"));
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

	@After
	public void tearDown() {
		this.irs.clearItemTypes();
	}
	
}