package pt.ulisboa.tecnico.softeng.broker.domain;


import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;




//public Adventure(Broker broker, LocalDate begin, LocalDate end, int age, Client client, int amount)
@RunWith(JMockit.class)
public class RentVehicleStateProcessMethodMockTest {
    private static final String ACTIVITY_REFERENCE = "activityReference";
    private static final String HOTEL_REFERENCE = "hotelReference";
    private static final String VEHICLE_REFERENCE = "vehicleReference";
    private static final String PAYMENT_CONFIRMATION = "paymentConfirmation";
    private final LocalDate begin = new LocalDate(2016, 12, 19);
    private final LocalDate end = new LocalDate(2016, 12, 21);
    private Broker broker;
    private  Client client;

    @Before
    public void setUp() {
        this.broker = new Broker("BR98", "Travel Light");
        this.client = new Client("BK01987654321",20, "123456789");
    }

    @Test
    public void processWithNoExceptions(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface) {
        new Expectations() {
            {
                BankInterface.processPayment("BK01987654321", 300);
                this.result = PAYMENT_CONFIRMATION;

            }
        };
        Adventure adventure = new Adventure(this.broker, this.begin, this.end, 20, this.client, 300);
        adventure.setState(Adventure.State.RESERVE_VEHICLE);
        adventure.process();

        Assert.assertEquals(VEHICLE_REFERENCE, adventure.getVehicleConfirmation());
        Assert.assertEquals(PAYMENT_CONFIRMATION, adventure.getPaymentConfirmation());
    }

}