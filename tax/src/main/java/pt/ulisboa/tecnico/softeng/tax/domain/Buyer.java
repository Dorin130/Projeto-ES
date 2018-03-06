package pt.ulisboa.tecnico.softeng.tax.domain;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class Buyer extends TaxPayer {

	public Buyer(String NIF, String name, String address) {
		super(NIF, name, address);

	}

	float taxReturn(int YEAR) {
    	if( YEAR < 1970)
    		throw new TaxException();
    	float taxReturn = 0;
    	for(Invoice i : this.getInvoices()) {
    		if(i.getDate().getYear() == YEAR)
    			taxReturn += i.getIVA()*0.05;
		}

		return taxReturn;
	}

}
