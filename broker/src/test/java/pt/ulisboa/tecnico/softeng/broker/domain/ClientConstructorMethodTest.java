package pt.ulisboa.tecnico.softeng.broker.domain;

import org.junit.Test;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

import static org.junit.Assert.assertEquals;

public class ClientConstructorMethodTest {

    private static final int AGE = 20;
    private static final String IBAN = "1234v";
    private static final String NIF = "123456789";


    @Test
    public void success() {
       Client client = new Client(IBAN, AGE, NIF);

        assertEquals(NIF, client.getNIF());
        assertEquals(AGE, client.getAge());
        assertEquals(IBAN, client.getIBAN());

    }


    @Test(expected = BrokerException.class)
    public void nullNIF() {
        new Client(IBAN, AGE, null);
    }

    @Test(expected = BrokerException.class)
    public void emptyNIF() {
        new Client(IBAN, AGE,"");
    }

    @Test(expected = BrokerException.class)
    public void nonNineDigitsNIF() {
        new Client(IBAN, AGE, "12345678");
    }

    @Test(expected = BrokerException.class)
    public void nullIban() {
        new Client(null, AGE, NIF);
    }

    @Test(expected = BrokerException.class)
    public void emptyIban() {
        new Client("", AGE, NIF);
    }
    @Test(expected = BrokerException.class)
    public void negativeAge() {
        new Client(IBAN, -1, NIF);
    }
}
