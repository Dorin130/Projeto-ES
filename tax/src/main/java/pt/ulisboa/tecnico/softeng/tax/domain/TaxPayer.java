package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public abstract class TaxPayer extends TaxPayer_Base{
	protected final Set<Invoice> invoices = new HashSet<>();

	public void init(IRS irs, String NIF, String name, String address) {
		checkArguments(irs, NIF, name, address);

		setIrs(irs);
		setNIF(NIF);
		setName(name);
		setAddress(address);
	}
	
	public void delete() {
		setIrs(null);
		setNIF(null);
		setName(null);
		setAddress(null);
		
		//for (Invoice invoices : getInvoiceSet()) {
		//	invoice.delete();
		//}
		this.invoices.clear();
		
		deleteDomainObject();
	}
	
	private void checkArguments(IRS irs, String NIF, String name, String address) {
		if (NIF == null || NIF.length() != 9) {
			throw new TaxException();
		}

		if (name == null || name.length() == 0) {
			throw new TaxException();
		}

		if (address == null || address.length() == 0) {
			throw new TaxException();
		}

		if (irs.getTaxPayerByNIF(NIF) != null) {
			throw new TaxException();
		}

	}

	public Invoice getInvoiceByReference(String invoiceReference) {
		if (invoiceReference == null || invoiceReference.isEmpty()) {
			throw new TaxException();
		}

		for (Invoice invoice : this.invoices) {
			if (invoice.getReference().equals(invoiceReference)) {
				return invoice;
			}
		}
		return null;
	}

	public void addInvoice(Invoice invoice) {
		invoices.add(invoice);
	}
}
