package pt.ulisboa.tecnico.softeng.broker.interfaces;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;

public class TaxInterface {
    public static String submitInvoice(String sellerNIF, String buyerNIF, String itemType, double value, LocalDate date) {
        return IRS.submitInvoice( new InvoiceData(sellerNIF, buyerNIF, itemType, value, date));
    }

    public static void cancelInvoice(String invoiceReference) {
        IRS.cancelInvoice(invoiceReference);
    }
}
