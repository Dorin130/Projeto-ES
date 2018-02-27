package pt.ulisboa.tecnico.softeng.tax.domain;

abstract public class TaxPayer {
	private String NIF;
	private String name;
	private String address;
	
	public TaxPayer(String NIF, String name, String address) {
		this.NIF = NIF;
		this.name = name;
		this.address = address;
	}
}
