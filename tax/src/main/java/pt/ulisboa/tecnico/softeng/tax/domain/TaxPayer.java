package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

abstract public class TaxPayer {
	private String NIF;
	private String name;
	private String address;

	private Set<Invoice> invoices = new HashSet<>();

	public TaxPayer(String NIF, String name, String address) {
		
		//Check arguments
		if(NIF.length() != 9 || IRS.getInstance().getTaxPayerByNIF(NIF) != null || name == null || name.equals("") || address == null || address.equals("")) {
			throw new TaxException();
		}
		
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
	
	public void addInvoice(Invoice invoice) {
		invoices.add(invoice);
	}
	
	Set<Invoice> getInvoices() {
		return invoices;
	}

	Invoice getInvoiceByReference(int invoice_reference) {
		return null;
	}//TODO implement this method
}
