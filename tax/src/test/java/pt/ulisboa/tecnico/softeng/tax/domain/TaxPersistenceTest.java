package pt.ulisboa.tecnico.softeng.tax.domain;

import org.junit.After;
import org.junit.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;


public class TaxPersistenceTest {
	/*private static final String BANK_NAME = "Money";
	private static final String BANK_CODE = "BK01";
	private static final String CLIENT_NAME = "João dos Anzóis";
	*/
	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		IRS.getIRS();
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		IRS.getIRS();
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		IRS.getIRS().delete();
	}

}
