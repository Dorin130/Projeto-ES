package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.tax.domain.ItemType;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;

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
		return new ItemType("comida", 12);
	}

	public TaxPayer getTaxPayerByNIF(String ITEM_TYPE) {
		return new Seller("123", "Zé", "Rua tantas");
	}

	public void submitInvoice(InvoiceData invoiceData) {
	}
}
