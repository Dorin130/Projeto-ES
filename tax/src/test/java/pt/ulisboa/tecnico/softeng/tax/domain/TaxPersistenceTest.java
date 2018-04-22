package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;


import java.util.HashSet;
import java.util.Set;


import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.Atomic.TxMode;


public class TaxPersistenceTest {

	private static final String ITEMTYPE_NAME = "XPTO";
	private static final int ITEMTYPE_TAX = 2;
	private static final String NIF_BUYER = "123456789";
	private static final String ADDRESS_BUYER = "address";
	private static final String NAME_BUYER = "João";
	private static final String NIF_SELLER = "100456789";
	private static final String ADDRESS_SELLER = "address2";
	private static final String NAME_SELLER = "Maria";
	
	Set<TaxPayer> taxPayersAdded = new HashSet<>();
	
	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		IRS irs = IRS.getIRS();

		ItemType itemtype = new ItemType(irs, ITEMTYPE_NAME, ITEMTYPE_TAX);
		
		taxPayersAdded.add(new Seller(irs, NIF_SELLER, NAME_SELLER, ADDRESS_SELLER));
		taxPayersAdded.add(new Buyer(irs, NIF_BUYER, NAME_BUYER, ADDRESS_BUYER));
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		IRS irs = IRS.getIRS();
		
		Set<TaxPayer> taxPayers = new HashSet<>(irs.getTaxpayerSet());
		assertEquals(2, taxPayers.size());
		assertEquals(taxPayersAdded, taxPayers);
		
		assertEquals(ITEMTYPE_NAME, irs.getItemTypeByName(ITEMTYPE_NAME).getName());
		assertEquals(ITEMTYPE_TAX, irs.getItemTypeByName(ITEMTYPE_NAME).getTax());
			
	}
	
	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		IRS.getIRS().delete();
	}

}