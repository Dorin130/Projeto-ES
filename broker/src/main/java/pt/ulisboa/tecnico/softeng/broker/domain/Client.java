package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class Client {

    private final String IBAN;
    private final int age;
    private final String NIF;

    public Client(String iban, int age, String nif) throws BrokerException{
        checkArguments(iban, age, nif);
        IBAN = iban;
        this.age = age;
        NIF = nif;
    }
    private void checkArguments(String iban, int age, String nif) {
        if (nif == null || nif.length() != 9) {
            throw new BrokerException();
        }
        if (age < 0) {
            throw  new BrokerException();
        }
        if(iban == null || iban.trim().length() == 0)
            throw new BrokerException();

    }
    public String getIBAN() {
        return this.IBAN;
    }

    public int getAge() {
        return this.age;
    }

    public String getNIF() {
        return this.NIF;
    }

}
