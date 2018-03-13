package pt.ulisboa.tecnico.softeng.tax.domain;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class Seller extends TaxPayer {

	public Seller(String NIF, String name, String address) {
		super(NIF, name, address);
	}

	public float toPay(int year) {
		if(year < 1970) {
			throw new TaxException();
		}
		float valueToPay = 0;
		for(Invoice invoice : this.getInvoices()) {
			if(invoice.getDate().getYear() == year){
				valueToPay += invoice.getIVA();
			}
		}
		return valueToPay;
	}
}
