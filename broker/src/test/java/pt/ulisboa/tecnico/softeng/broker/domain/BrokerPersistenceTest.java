package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class BrokerPersistenceTest extends BaseTest {
	private static final String TESTSTRING = "PERSISTENCETEST";
	private static final double TESTDOUBLE = 1.2;
	
	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		Broker broker = new Broker(BROKER_CODE, BROKER_NAME, BROKER_NIF_AS_SELLER, NIF_AS_BUYER, BROKER_IBAN);
		Client client = new Client(broker, CLIENT_IBAN, CLIENT_NIF, DRIVING_LICENSE, AGE);
		Adventure adventure1 = new Adventure(broker, this.begin, this.end, client, MARGIN);

		BulkRoomBooking bulk = new BulkRoomBooking(broker, NUMBER_OF_BULK, this.begin, this.end, NIF_AS_BUYER, CLIENT_IBAN);

		new Reference(bulk, REF_ONE);
		
		/* Using setters to test persistence */
		adventure1.setMargin(TESTDOUBLE);
		adventure1.setRentVehicle(true);
		adventure1.setCurrentAmount(TESTDOUBLE);
		adventure1.setInvoiceCancelled(true);
		adventure1.setInvoiceReference(TESTSTRING);
		adventure1.setRentingConfirmation(TESTSTRING);
		adventure1.setRentingCancellation(TESTSTRING);
		adventure1.setPaymentConfirmation(TESTSTRING);
		adventure1.setPaymentCancellation(TESTSTRING);
		adventure1.setRoomConfirmation(TESTSTRING);
		adventure1.setRoomCancellation(TESTSTRING);
		adventure1.setActivityConfirmation(TESTSTRING);
		adventure1.setActivityCancellation(TESTSTRING);
		
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		assertEquals(1, FenixFramework.getDomainRoot().getBrokerSet().size());

		List<Broker> brokers = new ArrayList<>(FenixFramework.getDomainRoot().getBrokerSet());
		Broker broker = brokers.get(0);

		assertEquals(BROKER_CODE, broker.getCode());
		assertEquals(BROKER_NAME, broker.getName());
		assertEquals(BROKER_NIF_AS_SELLER, broker.getNifAsSeller());
		assertEquals(NIF_AS_BUYER, broker.getNifAsBuyer());
		assertEquals(BROKER_IBAN, broker.getIban());
		assertEquals(1, broker.getAdventureSet().size());
		assertEquals(1, broker.getRoomBulkBookingSet().size());

		List<Adventure> adventures = new ArrayList<>(broker.getAdventureSet());
		Adventure adventure = adventures.get(0);
		assertEquals(TESTDOUBLE, adventure.getMargin(), 0.0f);
		assertTrue(adventure.getRentVehicle());
		assertEquals(TESTDOUBLE, adventure.getCurrentAmount(), 0.0f);
		assertTrue(adventure.getInvoiceCancelled());
		assertEquals(TESTSTRING, adventure.getInvoiceReference());
		assertEquals(TESTSTRING, adventure.getRentingConfirmation());
		assertEquals(TESTSTRING, adventure.getRentingCancellation());
		assertEquals(TESTSTRING, adventure.getPaymentConfirmation());
		assertEquals(TESTSTRING, adventure.getPaymentCancellation());
		assertEquals(TESTSTRING, adventure.getPaymentConfirmation());
		assertEquals(TESTSTRING, adventure.getPaymentCancellation());
		assertEquals(TESTSTRING, adventure.getRoomCancellation());
		assertEquals(TESTSTRING, adventure.getRoomConfirmation());
		assertEquals(TESTSTRING, adventure.getActivityCancellation());
		assertEquals(TESTSTRING, adventure.getActivityConfirmation());
		assertNotNull(adventure.getID());
		assertEquals(broker, adventure.getBroker());
		assertEquals(this.begin, adventure.getBegin());
		assertEquals(this.end, adventure.getEnd());
		assertEquals(AGE, adventure.getAge());
		assertEquals(CLIENT_IBAN, adventure.getIBAN());

		List<Client> clients = new ArrayList<>(broker.getClientSet());
		Client client = clients.get(0);
		assertEquals(CLIENT_IBAN, client.getIban());
		assertEquals(CLIENT_NIF, client.getNif());
		assertEquals(DRIVING_LICENSE, client.getDrivingLicense());
		assertEquals(AGE, client.getAge());

		assertEquals(Adventure.State.RESERVE_ACTIVITY, adventure.getState().getValue());
		assertEquals(0, adventure.getState().getNumOfRemoteErrors());

		List<BulkRoomBooking> bulks = new ArrayList<>(broker.getRoomBulkBookingSet());
		BulkRoomBooking bulk = bulks.get(0);

		assertNotNull(bulk);
		assertEquals(this.begin, bulk.getArrival());
		assertEquals(this.end, bulk.getDeparture());
		assertEquals(NUMBER_OF_BULK, bulk.getNumber());
		assertFalse(bulk.getCancelled());
		assertEquals(0, bulk.getNumberOfHotelExceptions());
		assertEquals(0, bulk.getNumberOfRemoteErrors());
		assertEquals(1, bulk.getReferenceSet().size());
		assertEquals(NIF_AS_BUYER, bulk.getBuyerNif());
		assertEquals(CLIENT_IBAN,bulk.getBuyerIban());
		
		List<Reference> references = new ArrayList<>(bulk.getReferenceSet());
		Reference reference = references.get(0);

		assertEquals(REF_ONE, reference.getValue());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Broker broker : FenixFramework.getDomainRoot().getBrokerSet()) {
			broker.delete();
		}
	}

}
