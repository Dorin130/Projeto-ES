package pt.ulisboa.tecnico.softeng.tax.domain;

public class ItemType {


	private String name;
	private double tax;
	
	public ItemType(String name, double tax) {
		this.name = name;
		this.tax = tax;
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