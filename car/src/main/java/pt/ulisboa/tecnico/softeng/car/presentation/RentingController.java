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
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.VehicleData;

@Controller
@RequestMapping(value = "/rentacars/{code}/vehicles/{plate}/rentings")
public class RentingController {
	private static Logger logger = LoggerFactory.getLogger(RentingController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String rentingForm(Model model, @PathVariable String code, @PathVariable String plate) {
		logger.info("rentingForm rentacarCode:{}, vehiclePlate:{}", code, plate);

		VehicleData vehicleData = CarInterface.getVehicleDataByPlate(code, plate);

		if (vehicleData == null) {
			model.addAttribute("error",
					"Error: There is no vehicle with plate " + plate + " in rentacar with code " + code);
			model.addAttribute("rentacar", new RentACarData());
			model.addAttribute("rentacars", CarInterface.getRentACars());
			return "rentacars";
		} else {
			model.addAttribute("renting", new RentingData());
			model.addAttribute("vehicle", vehicleData);
			return "rentings";
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public String rentingSubmit(Model model, @PathVariable String code, @PathVariable String plate,
			@ModelAttribute RentingData renting) {
		logger.info("rentingSubmit rentacarCode:{}, vehiclePlate:{}, Begin:{}, End:{}, buyerNif:{}, buyerIban:{}, drivingLicense:{}", code, plate,
				renting.getBegin(), renting.getEnd(), renting.getBuyerNif(), renting.getBuyerIban(), renting.getDrivingLicense());

		try {
			CarInterface.createRenting(code, plate, renting);
		} catch (CarException be) {
			model.addAttribute("error", "Error: it was not possible to rent the vehicle");
			model.addAttribute("renting", renting);
			model.addAttribute("vehicle", CarInterface.getVehicleDataByPlate(code, plate));
			return "rentings";
		}

		return "redirect:/rentacars/" + code + "/vehicles/" + plate + "/rentings";
	}
	
	@RequestMapping(value = "/{reference}", method = RequestMethod.GET)
    public String submitRentingCheckout(Model model, @PathVariable String code, @PathVariable String plate, @PathVariable String reference) {
		logger.info("checkoutForm rentacarCode:{}, vehiclePlate:{}, rentingReference:{}", code, plate, reference);

		RentingData rentingData = CarInterface.getRentingDataByReference(reference);

		if (rentingData == null) {
			model.addAttribute("error",
					"Error: There is no renting with reference " + reference);
			model.addAttribute("rentacar", new RentACarData());
			model.addAttribute("rentacars", CarInterface.getRentACars());
			return "rentacars";
		} else {
			model.addAttribute("renting", rentingData);
			return "rentCheckout";
		}        
    }
	
	@RequestMapping(value = "/{reference}", method = RequestMethod.POST)
    public String formRentingCheckout(Model model, @PathVariable String code, @PathVariable String plate, @PathVariable String reference,
			@ModelAttribute RentingData renting) {
		
        logger.info("checkoutSubmit rentacarCode:{}, vehiclePlate:{}, rentingReference:{}, kilometers:{}", code, plate, reference, renting.getKilometers());

        try {
			CarInterface.rentingCheckout(code, plate, reference, renting);
		} catch (CarException be) {
			model.addAttribute("error", "Error: it was not possible to checkout the renting");
			model.addAttribute("renting", renting);
			model.addAttribute("vehicle", CarInterface.getVehicleDataByPlate(code, plate));
			return "rentings";
		}
        return "redirect:/rentacars/" + code + "/vehicles/" + plate + "/rentings";

        
    }
}
