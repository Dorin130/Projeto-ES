package pt.ulisboa.tecnico.softeng.broker.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.broker.services.local.BrokerInterface;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.AdventureData;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.BrokerData;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.BrokerData.CopyDepth;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.ClientData;

@Controller
@RequestMapping(value = "/brokers/{brokerCode}/clients/{clientNif}/adventures")
public class AdventureController {
	private static Logger logger = LoggerFactory.getLogger(AdventureController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String showAdventures(Model model, @PathVariable String brokerCode, @PathVariable String clientNif) {
		logger.info("showAdventures code:{} nif:{}", brokerCode, clientNif);

		BrokerData brokerData = BrokerInterface.getBrokerDataByCode(brokerCode, CopyDepth.ADVENTURES);

		if (brokerData == null) {

			model.addAttribute("error", "Error: it does not exist a broker with the code " + brokerCode);
			model.addAttribute("broker", new BrokerData());
			model.addAttribute("brokers", BrokerInterface.getBrokers());
			return "brokers";
		}
		else {
			ClientData clientData = BrokerInterface.getClientDataByNif(brokerCode, clientNif );
			if(clientData == null) {
				model.addAttribute("error", "Error: it does not exist a client with the nif " + clientNif);
				model.addAttribute("broker", BrokerInterface.getBrokerDataByCode(brokerCode, CopyDepth.CLIENTS));
				model.addAttribute("client", new ClientData());
				return "broker";

			} else {
				model.addAttribute("adventure", new AdventureData());
				model.addAttribute("broker", brokerData);
				model.addAttribute("client", clientData);
				model.addAttribute("adventures", BrokerInterface.getClientAdventuresByNif(brokerCode, clientNif));
				return "client";
			}
		}
	}


	@RequestMapping(method = RequestMethod.POST)
	public String submitAdventure(Model model, @PathVariable String brokerCode, @PathVariable String clientNif,
			@ModelAttribute AdventureData adventureData) {
		logger.info("adventureSubmit brokerCode:{},clientNif:{}, begin:{}, end:{}, margin:{}, rentVehicle:{}", brokerCode, clientNif,
				adventureData.getBegin(), adventureData.getEnd(),  adventureData.getMargin(), adventureData.getRentVehicle());

		try {
			BrokerInterface.createAdventure(brokerCode, clientNif, adventureData);
			adventureData.setClientData(BrokerInterface.getClientDataByNif(brokerCode, clientNif));
		} catch (BrokerException be) {
			model.addAttribute("error", "Error: it was not possible to create the adventure");
			model.addAttribute("adventure", new AdventureData());
			model.addAttribute("broker", BrokerInterface.getBrokerDataByCode(brokerCode, CopyDepth.ADVENTURES));
			model.addAttribute("client", BrokerInterface.getClientDataByNif(brokerCode, clientNif));
			model.addAttribute("adventures", BrokerInterface.getClientAdventuresByNif(brokerCode, clientNif));
			return "client";
		}
		return "redirect:/brokers/" + brokerCode + "/clients/"+ clientNif + "/adventures";
	}

}
