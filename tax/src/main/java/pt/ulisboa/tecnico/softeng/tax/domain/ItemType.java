package pt.ulisboa.tecnico.softeng.tax.domain;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class ItemType {


	private String name;
	private double tax;
	
	public ItemType(String name, double tax) {
		if(name.trim().equals("") || name == null ||  tax < 0 )
			throw new TaxException();
		try {
			IRS.getInstance().getItemTypeByName(name);
		}catch(TaxException te) {
			this.name = name;
			this.tax = tax;
			IRS.getInstance().addItemType(this);
		}

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