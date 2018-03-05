package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class IRS {
	private static IRS irs = null;

	private final Set<TaxPayer> taxPayers = new HashSet<>();
	private final Set<ItemType> itemTypes = new HashSet<>();
	
	private IRS() {
	}

	public static IRS getInstance() {
		if (irs == null) {
			synchronized (IRS.class) {
				if (irs == null) {
					irs = new IRS();
				}
			}
		}
		return irs;
	}
	
	public void addTaxPayer(TaxPayer taxPayer) {
		taxPayers.add(taxPayer);
	}
	
	public void addItemType(ItemType itemType) {
		itemTypes.add(itemType);
	}
	
	public void clearTaxPayers() {
		taxPayers.clear();
	}
	
	public void clearItemTypes() {
		itemTypes.clear();
	}

	public ItemType getItemTypeByName(String ITEM_TYPE) {
		for(ItemType itemType : this.itemTypes) {
			if (itemType.getName().equals(ITEM_TYPE))
				return itemType;
		}
		throw new TaxException();
	}

	public TaxPayer getTaxPayerByNIF(String ITEM_TYPE) {
		for(TaxPayer taxPayer : this.taxPayers) {
			if (taxPayer.getNIF().equals(ITEM_TYPE))
				return taxPayer;
		}
		throw new TaxException();
	}

	public void submitInvoice(InvoiceData invoiceData) {
        new Invoice(invoiceData.getValue(), invoiceData.getDate(), invoiceData.getItemType(), invoiceData.getSellerNIF(), invoiceData.getBuyerNIF());
	}
}
