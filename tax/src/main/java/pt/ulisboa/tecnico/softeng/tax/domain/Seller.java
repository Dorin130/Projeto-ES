package pt.ulisboa.tecnico.softeng.tax.domain;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class Seller extends Seller_Base {
	public Seller(IRS irs, String NIF, String name, String address) {
		super();
		init(irs, NIF, name, address);
	}

	public double toPay(int year) {
		if (year < 1970) {
			throw new TaxException();
		}

		double result = 0;
		for (Invoice invoice : getInvoiceSet()) {
			if (!invoice.getCancelled() && invoice.getDate().getYear() == year) {
				result = result + invoice.getIva();
			}
		}
		return result;
	}

}
