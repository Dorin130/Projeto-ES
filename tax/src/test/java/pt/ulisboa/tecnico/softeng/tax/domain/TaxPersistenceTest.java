package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
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
	private static final Double VALUE = 1.35;
	private static final LocalDate DATE = new LocalDate(2018, 02, 13);
	
	Set<TaxPayer> taxPayersAdded = new HashSet<>();
	
	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		IRS irs = IRS.getIRS();

		Seller seller = new Seller(irs, NIF_SELLER, NAME_SELLER, ADDRESS_SELLER);
		taxPayersAdded.add(seller);
		Buyer buyer = new Buyer(irs, NIF_BUYER, NAME_BUYER, ADDRESS_BUYER);
		taxPayersAdded.add(buyer);
		
		ItemType itemtype = new ItemType(irs, ITEMTYPE_NAME, ITEMTYPE_TAX);
	
		new Invoice(VALUE, DATE, itemtype, seller, buyer);
		
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		IRS irs = IRS.getIRS();
		
		Set<TaxPayer> taxPayers = new HashSet<>(irs.getTaxpayerSet());
		assertEquals(2, taxPayers.size());
		assertEquals(taxPayersAdded, taxPayers);
		
		for(TaxPayer taxPayer : irs.getTaxpayerSet()) {
			assertNotNull(taxPayer.getIrs());
			assertNotNull(taxPayer.getAddress());
			assertNotNull(taxPayer.getNIF());
			assertNotNull(taxPayer.getName());
			
			Set<Invoice> invoices = taxPayer.getInvoiceSet();
			assertEquals(1, invoices.size());
			assertEquals(VALUE, invoices.iterator().next().getValue());
			assertEquals(DATE, invoices.iterator().next().getDate());
			assertEquals(false, invoices.iterator().next().getCancelled());
			assertNotNull(invoices.iterator().next().getIva());
			assertNotNull(invoices.iterator().next().getReference());
			
			ItemType itemType = invoices.iterator().next().getItemType();
			
			assertEquals(ITEMTYPE_NAME, itemType.getName());
			assertEquals(ITEMTYPE_TAX, itemType.getTax());
		}
		
		assertEquals(ITEMTYPE_NAME, irs.getItemTypeByName(ITEMTYPE_NAME).getName());
		assertEquals(ITEMTYPE_TAX, irs.getItemTypeByName(ITEMTYPE_NAME).getTax());
		assertEquals(1, irs.getItemTypeByName(ITEMTYPE_NAME).getInvoiceSet().size());
			
	}
	
	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		IRS.getIRS().delete();
	}

}