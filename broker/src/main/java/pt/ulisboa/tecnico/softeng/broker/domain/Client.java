package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class Client {
    private Broker broker;
    private final String IBAN;
    private final String NIF;
    private final String DRIVINGLICENSE;
    private final int age;


    public Client(Broker broker, String iban, String nif, String drivingLicense, int age) throws BrokerException {
        checkArguments(iban, age, nif, drivingLicense, broker);
        IBAN = iban;
        NIF = nif;
        DRIVINGLICENSE = drivingLicense;
        this.age = age;
        this.broker = broker;
    }
    private void checkArguments(String iban, int age, String nif, String drivingLicense, Broker broker) {
        if (nif == null || nif.length() != 9) {
            throw new BrokerException();
        }
        if(drivingLicense == null || drivingLicense.trim().length() == 0) {
            throw new BrokerException();
        }
        if (age < 0) {
            throw  new BrokerException();
        }
        if(iban == null || iban.trim().length() == 0) {
            throw new BrokerException();
        }
        if(broker == null) {
            throw  new BrokerException();
        }
    }
    public String getIBAN() { return this.IBAN; }

    public int getAge() {return this.age;}

    public String getNIF() { return this.NIF;}

    public Broker getBroker() { return broker; }

    public void setBroker(Broker broker) { this.broker = broker; }

    public String getDRIVINGLICENSE() { return this.DRIVINGLICENSE;}

}
