package pt.ulisboa.tecnico.softeng.tax.domain;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

import org.joda.time.LocalDate;

public class Invoice {
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

        IRS irs = IRS.getInstance();
        this.reference = Integer.toString(count++);
        this.value = VALUE;
        this.date = DATE;
        this.itemType = irs.getItemTypeByName(ITEM_TYPE);
        this.IVA = this.value * ((float) this.itemType.getTax());
        this.seller = (Seller) irs.getTaxPayerByNIF(SELLER);
        this.buyer = (Buyer) irs.getTaxPayerByNIF(BUYER);

        this.itemType.addInvoice(this);

    }

    private void checkArguments(float VALUE, LocalDate DATE, String ITEM_TYPE, String SELLER,String BUYER) {
        if(ITEM_TYPE == null || ITEM_TYPE.trim().equals("") ||
                SELLER == null || SELLER.trim().equals("") || SELLER.length() != 9 ||
                BUYER == null || BUYER.trim().equals("") || BUYER.length() != 9 || DATE.getYear() < 1970)
            throw new TaxException();
        try {
            int num;
            num = Integer.parseInt(SELLER);
            num = Integer.parseInt(BUYER);
        } catch (NumberFormatException nfe) {
            throw new TaxException();
        }
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getIVA() {
        return IVA;
    }

    public void setIVA(float IVA) {
        this.IVA = IVA;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Seller getSeller() {
        return seller;
    }

    public String getSellerNIF() {
        return seller.getNIF();
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public String getBuyerNIF() {
        return buyer.getNIF();
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public String getItemTypeName() {
        return itemType.getName();
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }
}
