package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

import java.util.HashMap;
import java.util.Map;

public class Seller extends Seller_Base {

	public Seller(IRS irs, String NIF, String name, String address) {
		checkArguments(irs, NIF, name, address);

		setNif(NIF);
		setName(name);
		setAddress(address);

		irs.addTaxPayer(this);
	}

	public void delete() {
		for (Invoice invoice : getInvoiceSet()) {
			invoice.delete();
		}

		super.delete();
	}

	public double toPay(int year) {
		if (year < 1970) {
			throw new TaxException();
		}

		double result = 0;
		for (Invoice invoice : getInvoiceSet()) {
			if (!invoice.isCancelled() && invoice.getDate().getYear() == year) {
				result = result + invoice.getIva();
			}
		}
		return result;
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

	public Map<Integer, Double> computeInvoices() {
		Map<Integer, Double> taxReturnByYear = new HashMap<>();
		for (int i = 1970; i <= new LocalDate().getYear(); i++) {
			double topay = toPay(i);
			if(topay != 0)
				taxReturnByYear.put(i, topay);
		}
		return taxReturnByYear;
	}
}
