package pt.ulisboa.tecnico.softeng.car.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.services.local.CarInterface;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentACarData;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.VehicleData;


@Controller
@RequestMapping(value = "/rentacars")
public class RentACarController {
	private static Logger logger = LoggerFactory.getLogger(RentACarController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String rentacarForm(Model model) {
		logger.info("rentacarForm");
		model.addAttribute("rentacar", new RentACarData());
		model.addAttribute("rentacars", CarInterface.getRentACars());
		return "rentacars";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String rentacarSubmit(Model model, @ModelAttribute RentACarData rentacar) {
		logger.info("rentacarSubmit name:{}, nif:{}, iban:{}", rentacar.getName(),
				rentacar.getNif(), rentacar.getIban());

		try {
			CarInterface.createRentACar(rentacar);
		} catch (CarException be) {
			model.addAttribute("error", "Error: it was not possible to create the rent-a-car");
			model.addAttribute("rentacar", new RentACarData());
			model.addAttribute("rentacars", CarInterface.getRentACars());
			return "rentacars";
		}

		return "redirect:/rentacars";
	}
	
	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	public String showVehicles(Model model, @PathVariable String code) {
		logger.info("showRentACar code:{}", code);
		RentACarData rentacar = CarInterface.getRentACarDataByCode(code);
		model.addAttribute("rentacar", rentacar);
		model.addAttribute("vehicles", rentacar.getVehicleSet() );
		model.addAttribute("vehicle", new VehicleData());
		return "carView";
	}

}
