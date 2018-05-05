package pt.ulisboa.tecnico.softeng.tax.services.local;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.Invoice;
import pt.ulisboa.tecnico.softeng.tax.domain.ItemType;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;
import pt.ulisboa.tecnico.softeng.tax.domain.TaxPayer;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.domain.*;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.ItemTypeData;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.TaxPayerData;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TaxInterface {
    @Atomic(mode = Atomic.TxMode.WRITE) //has to be write because it create a IRS the first time
    public static List<TaxPayerData> getTaxPayers() {
        return IRS.getIRSInstance().getTaxPayerSet().stream()
                .sorted((p1, p2) -> p1.getName().compareTo(p2.getName())).map(p -> new TaxPayerData(p, p.getClass().getSimpleName()))
                .collect(Collectors.toList());
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void createBuyer(TaxPayerData taxPayer) {
        new Buyer(IRS.getIRSInstance(), taxPayer.getNIF(), taxPayer.getName(), taxPayer.getAddress());

    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void createSeller(TaxPayerData taxPayer) {
        new Seller(IRS.getIRSInstance(), taxPayer.getNIF(), taxPayer.getName(), taxPayer.getAddress());
    }
    
    @Atomic(mode = Atomic.TxMode.WRITE)
    public static Map<Integer, Double> getTaxMap(String nif) {
        TaxPayer taxPayer = IRS.getIRSInstance().getTaxPayerByNIF(nif);
        if(taxPayer == null) return null;
        return taxPayer.computeInvoices();
    }
    
    @Atomic(mode = Atomic.TxMode.WRITE)
    public static TaxPayerData getTaxPayer(String nif) {
        TaxPayer taxPayer = IRS.getIRSInstance().getTaxPayerByNIF(nif);
        if(taxPayer == null) return null;
        return new TaxPayerData(taxPayer, taxPayer.getClass().getSimpleName());
    }


    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void createItemType(ItemTypeData itemTypeData) {
        new ItemType(IRS.getIRSInstance(), itemTypeData.getName(), itemTypeData.getTax());
    }

    @Atomic(mode = Atomic.TxMode.READ)
    public static Set<ItemTypeData> getItemTypes() {
        return IRS.getIRSInstance().getItemTypeSet().stream().map(it -> new ItemTypeData(it)).collect(Collectors.toSet());
    }

    
    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void submitInvoice(InvoiceData invoiceData) {
    	IRS.submitInvoice(invoiceData);
    }
    
    @Atomic(mode = Atomic.TxMode.READ)
    public static List<InvoiceData> getInvoicesData(TaxPayerData taxPayerData) {
    	String type = taxPayerData.getType();
    	String nif = taxPayerData.getNIF();
    	IRS irs = IRS.getIRSInstance();
    	if(type != null && type.equals("Seller")) {
    		Seller seller = (Seller) irs.getTaxPayerByNIF(nif);
    		return seller.getInvoiceSet().stream().sorted((p1, p2) -> p1.getDate().compareTo(p2.getDate())).map(p -> new InvoiceData(nif, p.getBuyer().getNif(), p.getItemType().getName(), p.getValue(), p.getDate())).collect(Collectors.toList());
    	}
    	else if(type != null && type.equals("Buyer")) {
    		Buyer buyer = (Buyer) irs.getTaxPayerByNIF(nif);
    		return buyer.getInvoiceSet().stream().sorted((p1, p2) -> p1.getDate().compareTo(p2.getDate())).map(p -> new InvoiceData(p.getSeller().getNif(), nif, p.getItemType().getName(), p.getValue(), p.getDate())).collect(Collectors.toList());
    	}
    	return null;
    }
}
