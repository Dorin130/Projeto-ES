package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

abstract public class TaxPayer {
	private String NIF;
	private String name;
	private String address;
	
	private IRS irs = IRS.getInstance();
	
	private Set<Invoice> invoices = new HashSet<>();

	public TaxPayer(String NIF, String name, String address) {
		checkArguments(NIF, name, address);
		
		this.setNIF(NIF);
		this.setName(name);
		this.setAddress(address);
		
		this.irs.addTaxPayer(this);
	}
	
	private void checkArguments(String NIF, String name, String address) {
		if(NIF.length() != 9 || irs.getTaxPayerByNIF(NIF) != null ||
				name == null || name.trim().equals("") ||
				address == null || address.trim().equals("")) {
			throw new TaxException();
		}
		try {
			Integer.parseInt(NIF);
		}
		catch (NumberFormatException nfe) {
			throw new TaxException();
		}
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

	Invoice getInvoiceByReference(String invoice_reference) {
		checkInvoiceReference(invoice_reference);
		
		for (Invoice invoice : this.invoices) {
			if(invoice.getReference().equals(invoice_reference)) {
				return invoice;
			}
		}
		return null;
	}
	
	public void checkInvoiceReference(String reference) {
		if(reference == null || reference.trim().equals("")) {
			throw new TaxException();
		}
	}
}
