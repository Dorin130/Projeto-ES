package pt.ulisboa.tecnico.softeng.tax.services.local;

import pt.ist.fenixframework.Atomic;
import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;
import pt.ulisboa.tecnico.softeng.tax.domain.TaxPayer;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.TaxPayerData;

import java.util.List;
import java.util.Map;
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
    public static void computeInvoices(TaxPayerData taxPayer) {
        new Seller(IRS.getIRSInstance(), taxPayer.getNIF(), taxPayer.getName(), taxPayer.getAddress());
    }
    @Atomic(mode = Atomic.TxMode.WRITE)
    public static Map<Integer, Double> getTaxMap(String nif) {
        return IRS.getIRSInstance().getTaxPayerByNIF(nif).computeInvoices();
    }
    @Atomic(mode = Atomic.TxMode.WRITE)
    public static TaxPayerData getTaxPayer(String nif) {
        TaxPayer taxPayer = IRS.getIRSInstance().getTaxPayerByNIF(nif);
        return new TaxPayerData(taxPayer, taxPayer.getClass().getSimpleName());
    }
}
