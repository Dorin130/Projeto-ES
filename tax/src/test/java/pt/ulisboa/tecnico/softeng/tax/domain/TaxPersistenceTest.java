package pt.ulisboa.tecnico.softeng.tax.domain;

import org.junit.After;
import org.junit.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;

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

	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {

	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		//FenixFramework.getDomainRoot().getIRS().clearAll();
	}

}
