package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashSet;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class ItemType {


	private String name;
	private double tax;
	private HashSet<Invoice> invoices = new HashSet<Invoice>();
	
	public ItemType(String name, double tax) {

		if(name == null || name.trim().equals("") ||   tax < 0 || tax > 100 )
			throw new TaxException();

		this.name = name;
		this.tax = tax;
		IRS.getInstance().addItemType(this);
	}

	public void addInvoice(Invoice invoice) {
		invoices.add(invoice);
	}

	public String getName() {
		return name;
	}

	public double getTax() {
		return tax;
	}


}