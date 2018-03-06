package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashSet;
import java.util.Set;

public class Seller extends TaxPayer {
	private final Set<Invoice> invoices = new HashSet<>();

	public Seller(String NIF, String name, String address) {
		super(NIF, name, address);
	}

	public void addInvoice(Invoice invoice) {invoices.add(invoice);}
	public  void getInvoiceByReference(String invoiceReference) {}

}
