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
@RequestMapping(value = "/rentacars/{code}/vehicles")
public class VehicleController {
	private static Logger logger = LoggerFactory.getLogger(VehicleController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String vehicleForm(Model model, @PathVariable String code) {
		logger.info("vehicleForm");
		RentACarData rentacar = CarInterface.getRentACarDataByCode(code);
		model.addAttribute("rentacar", rentacar);
		model.addAttribute("vehicle", new VehicleData());
		model.addAttribute("vehicles", rentacar.getVehicleSet());
		return "carView";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String vehicleSubmit(Model model, @ModelAttribute VehicleData vehicledata,@PathVariable String code) {
		logger.info("rentacarSubmit type: {} plate:{}, km:{}, price:{}", vehicledata.getType(), 
				vehicledata.getPlate(), vehicledata.getKilometers(), vehicledata.getPrice());
		try {
			CarInterface.createVehicle(vehicledata , code);
		} catch (CarException be) {
      
			RentACarData rentacar = CarInterface.getRentACarDataByCode(code);
			model.addAttribute("rentacar", rentacar);
			model.addAttribute("error", "Error: it was not possible to create the vehicle");
			model.addAttribute("vehicle", new VehicleData());
			model.addAttribute("vehicles", rentacar.getVehicleSet());
			return "carView";
		}

		return "redirect:/rentacars/{code}/vehicles";
	}

}
