package pt.ulisboa.tecnico.softeng.broker.domain;

import mockit.Injectable;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

import static org.junit.Assert.assertEquals;

public class ClientConstructorMethodTest {

    private static final int AGE = 20;
    private static final String IBAN = "1234v";
    private static final String NIF = "123456789";
    private static final String DRIVING_LICENSE = "IMT1234";

    @Injectable
    private Broker broker;

    @Test
    public void success() {
       Client client = new Client(broker, IBAN, NIF,DRIVING_LICENSE, AGE);

        assertEquals(NIF, client.getNIF());
        assertEquals(AGE, client.getAge());
        assertEquals(IBAN, client.getIBAN());
        assertEquals(DRIVING_LICENSE, client.getDRIVINGLICENSE());

    }


    @Test(expected = BrokerException.class)
    public void nullNIF() {
        new Client(broker, IBAN, null, DRIVING_LICENSE, AGE);
    }

    @Test(expected = BrokerException.class)
    public void emptyNIF() {
        new Client(broker, IBAN, "",DRIVING_LICENSE, AGE);
    }

    @Test(expected = BrokerException.class)
    public void nonNineDigitsNIF() {
        new Client(broker, IBAN,"12345678",DRIVING_LICENSE, AGE);
    }

    @Test(expected = BrokerException.class)
    public void nullIban() {
        new Client(broker,null, NIF, DRIVING_LICENSE, AGE);
    }

    @Test(expected = BrokerException.class)
    public void emptyIban() {
        new Client(broker,"",NIF, DRIVING_LICENSE, AGE);
    }

    @Test(expected = BrokerException.class)
    public void negativeAge() {
        new Client(broker, IBAN, NIF,DRIVING_LICENSE, -1);
    }

    @Test(expected = BrokerException.class)
    public void nullDrivingLicense() { new Client(broker, IBAN, NIF,null, AGE);}

    @Test(expected = BrokerException.class)
    public void emptyDrivingLicense() { new Client(broker, IBAN, NIF,"", AGE);}

    @Test(expected = BrokerException.class)
    public void nullBroker() { new Client(null, IBAN, NIF,DRIVING_LICENSE, AGE);}
}
