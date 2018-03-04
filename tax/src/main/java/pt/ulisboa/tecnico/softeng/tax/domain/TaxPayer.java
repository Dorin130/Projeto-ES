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

}
