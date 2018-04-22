package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;


public class TaxPersistenceTest {
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
		
		taxPayersAdded.add(new Seller(irs, NIF_SELLER, NAME_SELLER, ADDRESS_SELLER));
		taxPayersAdded.add(new Buyer(irs, NIF_BUYER, NAME_BUYER, ADDRESS_BUYER));
	
		//Invoice invoice = new Invoice(VALUE, DATE, itemType, seller, buyer);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		IRS irs = IRS.getIRS();
		
		Set<TaxPayer> taxPayers = new HashSet<>(irs.getTaxpayerSet());
		assertEquals(2, taxPayers.size());
		assertEquals(taxPayersAdded, taxPayers);
		
		//ciclo for?
		//Set<Invoice> invoices = new HashSet<>(irs.getTaxpayerSet());
		//assertEquals(1, invoices.size());
		//assertEquals(VALUE, invoices.iterator().next().getValue());
		//Fazer para os restantes atributos
		
		//Set<Invoice> itemTypes = new HashSet<>(irs.getTaxpayerSet());
		//assertEquals(1, invoices.size());
		//assertEquals(VALUE, invoices.iterator().next().getValue());
		//Fazer para os restantes atributos
	}
	
	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		IRS.getIRS().delete();
	}

}
