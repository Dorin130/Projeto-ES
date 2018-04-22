package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class Invoice extends Invoice_Base{
	private static int counter = 0;

	Invoice(double value, LocalDate date, ItemType itemType, Seller seller, Buyer buyer) {
		checkArguments(value, date, itemType, seller, buyer);

		setReference(Integer.toString(++Invoice.counter));
		setValue(value);
		setDate(date);
		setItemType(itemType);
		setIva(value * itemType.getTax() / 100);
		setCancelled(false);

		addTaxpayer(seller);
		addTaxpayer(buyer);
	}

	private void checkArguments(double value, LocalDate date, ItemType itemType, Seller seller, Buyer buyer) {
		if (value <= 0.0f) {
			throw new TaxException();
		}

		if (date == null || date.getYear() < 1970) {
			throw new TaxException();
		}

		if (itemType == null) {
			throw new TaxException();
		}

		if (seller == null) {
			throw new TaxException();
		}

		if (buyer == null) {
			throw new TaxException();
		}
	}

	public void delete() {
		for(TaxPayer taxPayer : getTaxpayerSet()) {
			removeTaxpayer(taxPayer);
		}
		setItemType(null);
		deleteDomainObject();
	}


}
