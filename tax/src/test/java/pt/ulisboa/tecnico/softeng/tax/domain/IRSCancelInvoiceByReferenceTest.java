package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.fail;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mockit.Expectations;
import mockit.FullVerifications;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.runner.RunWith;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

@RunWith(JMockit.class)
public class IRSCancelInvoiceByReferenceTest {
	private String invoiceReference = "REFERENCE";
	
	@Before
	public void setUp() {
	}

	@Test
	public void success(@Mocked final Invoice invoice) {
		IRS irs = IRS.getIRS();
		new Expectations(IRS.class) {{
				irs.getInvoiceByReference(invoiceReference);
				result = invoice;
				times = 1;
		}};

		IRS.cancelInvoice(this.invoiceReference);
		
		new FullVerifications() {
			{
				invoice.cancel();
				times = 1;
			}
		};
	}

	@Test(expected = TaxException.class)
	public void nullReference() {
		IRS irs = IRS.getIRS();
		new Expectations(IRS.class) {{
				irs.getInvoiceByReference(withNull());
				result = new TaxException();
		}};
		
		IRS.cancelInvoice(null);
	}

	@Test(expected = TaxException.class)
	public void emptyReference() {
		IRS irs = IRS.getIRS();
		new Expectations(IRS.class) {{
				irs.getInvoiceByReference("");
				result = new TaxException();
		}};
		
		IRS.cancelInvoice("");
	}

	@Test
	public void doesNotExist() {
		IRS irs = IRS.getIRS();
		new Expectations(IRS.class) {{
				irs.getInvoiceByReference("XPTO");
				result = null;
		}};
		try {
			IRS.cancelInvoice("XPTO");
		} catch (NullPointerException npe) {
			fail();
		}
	}

	@After
	public void tearDown() {
		IRS.getIRS().clearAll();
	}

}
