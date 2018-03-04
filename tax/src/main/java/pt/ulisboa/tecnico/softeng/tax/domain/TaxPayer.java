package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashSet;
import java.util.Set;

abstract public class TaxPayer {
	private String NIF;
	private String name;
	private String address;

	private Set<Invoice> invoices = new HashSet<>();

	public TaxPayer(String NIF, String name, String address) {
		this.setNIF(NIF);
		this.setName(name);
		this.setAddress(address);
	}

	public String getNIF() {
		return NIF;
	}

	public void setNIF(String nIF) {
		NIF = nIF;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	void addInvoice(Invoice invoice) {invoices.add(invoice);}

	Set<Invoice> getInvoices() {return invoices;}

	Invoice getInvoiceByReference(int invoice_reference) {return null;}//TODO implement this method
}
