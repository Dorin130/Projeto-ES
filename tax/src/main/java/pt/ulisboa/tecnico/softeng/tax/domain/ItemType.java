package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashSet;

public class ItemType {


	private String name;
	private double tax;
	private HashSet<Invoice> invoices = new HashSet<Invoice>();
	
	public ItemType(String name, double tax) {
		this.name = name;
		this.tax = tax;
	}

	public void addInvoice(Invoice invoice) {
		invoices.add(invoice);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;

	}

	public double getTax() {
		return tax;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}
}