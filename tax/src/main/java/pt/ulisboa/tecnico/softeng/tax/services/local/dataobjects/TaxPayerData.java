package pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.tax.domain.TaxPayer;

import java.util.List;


public class TaxPayerData {
    private String NIF;
    private String name;
    private String address;
    private String type;
    private List<Pair> balanceByYear;

    public TaxPayerData() {

    }

    public TaxPayerData(TaxPayer tp, String type) {
        this.NIF = tp.getNif();
        this.name = tp.getName();
        this.address = tp.getAddress();
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNIF() {
        return NIF;
    }

    public void setNIF(String NIF) {
        this.NIF = NIF;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Pair> getBalanceByYear() {
        return balanceByYear;
    }

    public void setBalanceByYear(List<Pair> balanceByYear) {
        this.balanceByYear = balanceByYear;
    }
}


class Pair {
    int year;
    double value;

    public Pair(int year, double value) {
        this.year = year;
        this.value = value;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }


}
