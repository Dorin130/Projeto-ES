package pt.ulisboa.tecnico.softeng.broker.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.broker.domain.Broker;
import pt.ulisboa.tecnico.softeng.broker.domain.Client;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.broker.services.local.BrokerInterface;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.BrokerData;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.ClientData;

import java.util.List;

@Controller
@RequestMapping(value = "/brokers")
public class BrokerController {
	private static Logger logger = LoggerFactory.getLogger(BrokerController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String brokerForm(Model model) {
		logger.info("brokerForm");
		model.addAttribute("broker", new BrokerData());
		model.addAttribute("brokers", BrokerInterface.getBrokers());
		return "brokers";
	}

	@RequestMapping(value="/{brokerCode}/adventures", method = RequestMethod.GET)
	public String showBrokerAdventures(Model model, @PathVariable String brokerCode) {
		logger.info("showBrokerAdventures code:{}", brokerCode);

		BrokerData brokerData = BrokerInterface.getBrokerDataByCode(brokerCode, BrokerData.CopyDepth.ADVENTURES);

		if (brokerData == null) {

			model.addAttribute("error", "Error: it does not exist a broker with the code " + brokerCode);
			model.addAttribute("broker", new BrokerData());
			model.addAttribute("brokers", BrokerInterface.getBrokers());
			return "brokers";
		}
		else {
			model.addAttribute("broker", brokerData);
			return "adventures";
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public String brokerSubmit(Model model, @ModelAttribute BrokerData brokerData) {
		logger.info("brokerSubmit name:{}, code:{}, iban:{}", brokerData.getName(), brokerData.getCode(), brokerData.getIban());

		try {
			BrokerInterface.createBroker(brokerData);
		} catch (BrokerException be) {
			model.addAttribute("error", "Error: it was not possible to create the broker");
			model.addAttribute("broker", brokerData);
			model.addAttribute("brokers", BrokerInterface.getBrokers());
			return "brokers";
		}

		return "redirect:/brokers";
	}

}
