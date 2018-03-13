package pt.ulisboa.tecnico.softeng.tax.domain;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

import org.joda.time.LocalDate;

public class Invoice {
    private static final IRS irs = IRS.getInstance();
    private static int count = 0;

    private String reference;
    private float value;
    private float IVA;
    private LocalDate date;
    private Seller seller;
    private Buyer buyer;
    private ItemType itemType;

    public Invoice(float VALUE, LocalDate DATE, String ITEM_TYPE, String SELLER,String BUYER) {
        checkArguments(VALUE,DATE,ITEM_TYPE,SELLER,BUYER);

        this.reference = Integer.toString(count++);
        this.value = VALUE;
        this.date = DATE;
        this.itemType = irs.getItemTypeByName(ITEM_TYPE);
        this.IVA = this.value * (((float) this.itemType.getTax())/100f);
        this.seller = (Seller) irs.getTaxPayerByNIF(SELLER);
        this.buyer = (Buyer) irs.getTaxPayerByNIF(BUYER);

        this.itemType.addInvoice(this);
        this.buyer.addInvoice(this);
        this.seller.addInvoice(this);

    }

    private void checkArguments(float VALUE, LocalDate DATE, String ITEM_TYPE, String SELLER,String BUYER) {
        if(ITEM_TYPE == null || ITEM_TYPE.trim().equals("") ||
                SELLER == null || SELLER.trim().equals("") || SELLER.length() != 9 ||
                BUYER == null || BUYER.trim().equals("") || BUYER.length() != 9 || DATE.getYear() < 1970)
            throw new TaxException();
        try {
            
            Integer.parseInt(SELLER);
            Integer.parseInt(BUYER);
        } catch (NumberFormatException nfe) {
            throw new TaxException();
        }
    }

    public String getReference() {
        return reference;
    }

    public float getValue() {
        return value;
    }

    public float getIVA() {
        return IVA;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getSellerNIF() {
        return seller.getNIF();
    }

    public String getBuyerNIF() {
        return buyer.getNIF();
    }

    public String getItemTypeName() {
        return itemType.getName();
    }
}
