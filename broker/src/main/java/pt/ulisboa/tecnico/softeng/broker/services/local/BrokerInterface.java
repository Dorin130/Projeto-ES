package pt.ulisboa.tecnico.softeng.broker.services.local;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure;
import pt.ulisboa.tecnico.softeng.broker.domain.Broker;
import pt.ulisboa.tecnico.softeng.broker.domain.BulkRoomBooking;
import pt.ulisboa.tecnico.softeng.broker.domain.Client;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.AdventureData;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.BrokerData;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.BrokerData.CopyDepth;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.BulkData;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.ClientData;

public class BrokerInterface {

	@Atomic(mode = TxMode.READ)
	public static List<BrokerData> getBrokers() {
		List<BrokerData> brokers = new ArrayList<>();
		for (Broker broker : FenixFramework.getDomainRoot().getBrokerSet()) {
			brokers.add(new BrokerData(broker, CopyDepth.SHALLOW));
		}
		return brokers;
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createBroker(BrokerData brokerData) {
		new Broker(brokerData.getCode(), brokerData.getName(), brokerData.getNifAsSeller(), brokerData.getNifAsBuyer(),
				brokerData.getIban());
	}


	@Atomic(mode = TxMode.READ)
	public static ClientData getClientDataByNif(String brokerCode, String nif) {
		Broker broker = getBrokerByCode(brokerCode);
		Client client = getClientByNif(broker, nif);
		if(client != null) {
				return new ClientData(client);
			}
		return null;
	}

	@Atomic(mode = TxMode.READ)
	public static BrokerData getBrokerDataByCode(String brokerCode, CopyDepth depth) {
		Broker broker = getBrokerByCode(brokerCode);

		if (broker != null) {
			return new BrokerData(broker, depth);
		} else {
			return null;
		}
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static void createClient(String brokerCode, ClientData clientData) {
		new Client(getBrokerByCode(brokerCode), clientData.getIban(), clientData.getNif(), clientData.getDrivingLicense(), clientData.getAge());
	}
	

	@Atomic(mode = TxMode.WRITE)
	public static void createAdventure(String brokerCode, String clientNif, AdventureData adventureData) {
		Broker broker = getBrokerByCode(brokerCode);
		Client client = getClientByNif(broker, clientNif);
		new Adventure(broker, adventureData.getBegin(), adventureData.getEnd(), client, adventureData.getMargin(), adventureData.getRentVehicle());
	}

	@Atomic(mode = TxMode.READ)
	public static Set<AdventureData> getClientAdventuresByNif(String brokerCode, String nif) {
		BrokerData brokerData = getBrokerDataByCode(brokerCode, CopyDepth.ADVENTURES);
		if(brokerData == null) return null;
		return  brokerData.getAdventures().stream().filter(c -> c.getClientData().getNif().equals(nif)).collect(Collectors.toSet());
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createBulkRoomBooking(String brokerCode, BulkData bulkData) {
		new BulkRoomBooking(getBrokerByCode(brokerCode), bulkData.getNumber() != null ? bulkData.getNumber() : 0,
				bulkData.getArrival(), bulkData.getDeparture(), bulkData.getBuyerNif(), bulkData.getBuyerIban());

	}

	@Atomic(mode = TxMode.READ)
	private static Broker getBrokerByCode(String code) {
			return FenixFramework.getDomainRoot().getBrokerSet().stream().
					filter(b -> b.getCode().equals(code)).findFirst().orElse(null);

	}

	@Atomic(mode = TxMode.READ)
	private static Client getClientByNif(Broker broker, String nif) {
		if(broker == null) return null;
		return broker.getClientSet().stream().
				filter(c -> c.getNif().equals(nif)).findFirst().orElse(null);
	}

}
