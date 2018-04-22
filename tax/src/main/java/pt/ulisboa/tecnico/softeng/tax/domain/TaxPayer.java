package pt.ulisboa.tecnico.softeng.tax.domain;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public abstract class TaxPayer extends TaxPayer_Base{
		public void init(IRS irs, String NIF, String name, String address) {
		checkArguments(irs, NIF, name, address);

		setIrs(irs);
		setNIF(NIF);
		setName(name);
		setAddress(address);
	}
	
	public void delete() {
		setIrs(null);
		
		for (Invoice invoice : getInvoiceSet()) {
			invoice.delete();
		}
		
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

		for (Invoice invoice : getInvoiceSet()) {
			if (invoice.getReference().equals(invoiceReference)) {
				return invoice;
			}
		}
		return null;
	}
}
