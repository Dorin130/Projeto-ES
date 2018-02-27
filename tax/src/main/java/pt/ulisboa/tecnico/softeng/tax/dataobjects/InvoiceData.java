package pt.ulisboa.tecnico.softeng.tax.dataobjects;

import org.joda.time.LocalDate;

public class InvoiceData {
	private String sellerNIF;
	private String buyerNIF;
	private String itemType;
	private float value;
	private LocalDate date;
	
	public InvoiceData () {}
	
	public InvoiceData(String _sellerNIF, String _buyerNIF, String _itemType, float _value, LocalDate _date) {
		sellerNIF = _sellerNIF;
		buyerNIF = _buyerNIF;
		itemType = _itemType;
		value = _value;
		date = _date;
	}

	public String getSellerNIF() {
		return sellerNIF;
	}

	public void setSellerNIF(String sellerNIF) {
		this.sellerNIF = sellerNIF;
	}

	public String getBuyerNIF() {
		return buyerNIF;
	}

	public void setBuyerNIF(String buyerNIF) {
		this.buyerNIF = buyerNIF;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

}
