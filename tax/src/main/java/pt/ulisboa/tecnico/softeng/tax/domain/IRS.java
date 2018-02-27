package pt.ulisboa.tecnico.softeng.tax.domain;


import pt.ulisboa.tecnico.softeng.tax.domain.ItemType;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;

public class IRS 
{
    public IRS() {}
    
    public static ItemType getItemTypeByName(String ITEM_TYPE) {
    	return new ItemType();
    }
    
    public static TaxPayer getTaxPayerByNIF(String ITEM_TYPE) {
    	return new TaxPayer();
    }
    
    public static void submitInvoice(InvoiceData invoiceData) {
    }
}
