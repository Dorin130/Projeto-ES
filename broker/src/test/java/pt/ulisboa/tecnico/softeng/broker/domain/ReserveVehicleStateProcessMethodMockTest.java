package pt.ulisboa.tecnico.softeng.broker.domain;


import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;


//public Adventure(Broker broker, LocalDate BEGINADVENTURE, LocalDate ENDADVENTURE, int age, Client client, int amount)
@RunWith(JMockit.class)
public class ReserveVehicleStateProcessMethodMockTest {
    private static final int MAXTRIES = 5;
    private static final String ACTIVITY_REFERENCE = "activityReference";
    private static final String HOTEL_REFERENCE = "hotelReference";
    private static final String VEHICLE_REFERENCE = "vehicleReference";
    private static final String PAYMENT_CONFIRMATION = "paymentConfirmation";
    private final LocalDate BEGINADVENTURE = new LocalDate(2016, 12, 19);
    private final LocalDate ENDADVENTURE = new LocalDate(2016, 12, 21);
    private  Client client;
    private String NIF = "123456789";
    private String IBAN = "BK01987654321";
    private int AGE = 20;
    private static final String DRIVING_LICENSE = "IMT1234";
    private  String LICENSE1="aa-00-11";
    private Adventure adventure;

    @Injectable
    private Broker broker;


    @Before
    public void setUp() {
        this.client = new Client(this.broker, IBAN,NIF, DRIVING_LICENSE, AGE);
        this.adventure = new Adventure(this.broker, this.BEGINADVENTURE, this.ENDADVENTURE, this.client, 300, true);
        this.adventure.setState(Adventure.State.RESERVE_VEHICLE);
    }

    @Test
    public void processWithNoExceptions(@Mocked final BankInterface bankInterface, @Mocked final CarInterface carInterface) {
        new Expectations() {
            {
                CarInterface.processRenting(LICENSE1, BEGINADVENTURE, ENDADVENTURE, NIF, IBAN);
                this.result = VEHICLE_REFERENCE;
                BankInterface.processPayment("BK01987654321", 300);
                this.result = PAYMENT_CONFIRMATION;

            }
        };
        this.adventure.process();

        Assert.assertEquals(VEHICLE_REFERENCE, this.adventure.getVehicleConfirmation());
        Assert.assertEquals(PAYMENT_CONFIRMATION, this.adventure.getPaymentConfirmation());
        Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
    }

    @Test
    public void carException(@Mocked final CarInterface carInterface) {
        new Expectations() {
            {
                CarInterface.processRenting(LICENSE1, BEGINADVENTURE, ENDADVENTURE, NIF, IBAN);
                this.result = new CarException();
            }
        };

        this.adventure.process();
        Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
    }

    @Test
    public void bankException(@Mocked final CarInterface carInterface, @Mocked final BankInterface Interface) {
        new Expectations() {
            {
                CarInterface.processRenting(LICENSE1, BEGINADVENTURE, ENDADVENTURE, NIF, IBAN);
                this.result = VEHICLE_REFERENCE;
                BankInterface.processPayment("BK01987654321", 300);
                this.result = new BankException();
            }
        };
        this.adventure.process();

        Assert.assertEquals(Adventure.State.CANCELLED, adventure.getState());
    }

    @Test
    public void singleCarRemoteAccessException(@Mocked final CarInterface carInterface, @Mocked final BankInterface Interface) {
        new Expectations() {
            {
                CarInterface.processRenting(LICENSE1, BEGINADVENTURE, ENDADVENTURE, NIF, IBAN);
                this.result = new RemoteAccessException();
                BankInterface.processPayment("BK01987654321", 300);
                this.result = PAYMENT_CONFIRMATION;
            }
        };
        this.adventure.process();
        Assert.assertEquals(Adventure.State.RESERVE_VEHICLE, adventure.getState());
    }

    @Test
    public void maxCarRemoteAccessException(@Mocked final CarInterface carInterface, @Mocked final BankInterface Interface) {
        new Expectations() {
            {
                CarInterface.processRenting(LICENSE1, BEGINADVENTURE, ENDADVENTURE, NIF, IBAN);
                this.result = new RemoteAccessException();
                BankInterface.processPayment("BK01987654321", 300);
                this.result = PAYMENT_CONFIRMATION;
            }
        };
        for(int i=0; i < MAXTRIES+1; i++) {
            this.adventure.process();
        }
        Assert.assertEquals(Adventure.State.CANCELLED, adventure.getState());
    }

    @Test
    public void maxMinusOneCarRemoteAccessException(@Mocked final CarInterface carInterface, @Mocked final BankInterface Interface) {
        new Expectations() {
            {
                CarInterface.processRenting(LICENSE1, BEGINADVENTURE, ENDADVENTURE, NIF, IBAN);
                this.result = new RemoteAccessException();
                BankInterface.processPayment("BK01987654321", 300);
                this.result = PAYMENT_CONFIRMATION;
            }
        };
        for(int i=0; i < MAXTRIES; i++) {
            this.adventure.process();
        }
        Assert.assertEquals(Adventure.State.CONFIRMED, adventure.getState());
    }
    @Test
    public void fourCarRemoteAccessExceptionOneSuccess(@Mocked final CarInterface carInterface, @Mocked final BankInterface Interface) {
        new Expectations() {
            {
                CarInterface.processRenting(LICENSE1, BEGINADVENTURE, ENDADVENTURE, NIF, IBAN);
                this.result = new Delegate() {
                    int i = 0;

                    public String delegate() {
                        if (this.i < 4) {
                            this.i++;
                            throw new RemoteAccessException();
                        } else {
                            return VEHICLE_REFERENCE;
                        }
                    }
                };
                this.times = 5;

                BankInterface.processPayment("BK01987654321", 300);
                this.result = PAYMENT_CONFIRMATION;

            }
        };
        for(int i=0; i < MAXTRIES; i++) {
            this.adventure.process();
        }
        Assert.assertEquals(Adventure.State.CONFIRMED, adventure.getState());
    }

    @Test
    public void oneRemoteAccessExceptionOneCarException(@Mocked final CarInterface carInterface, @Mocked final BankInterface Interface) {
        new Expectations() {
            {
                CarInterface.processRenting(LICENSE1, BEGINADVENTURE, ENDADVENTURE, NIF, IBAN);

                this.result = new Delegate() {
                    int i = 0;

                    public String delegate() {
                        if (this.i < 1) {
                            this.i++;
                            throw new RemoteAccessException();
                        } else {
                            throw new CarException();
                        }
                    }
                };
                this.times = 2;

            }
        };

        this.adventure.process();
        this.adventure.process();

        Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
    }

}