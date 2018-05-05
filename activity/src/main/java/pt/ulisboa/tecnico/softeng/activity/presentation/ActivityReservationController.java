package pt.ulisboa.tecnico.softeng.activity.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.services.local.ActivityInterface;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityOfferData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData;

@Controller
@RequestMapping(value = "/providers/{codeProvider}/activities/{codeActivity}/offers/{reference}/reservations")
public class ActivityReservationController {
	private static Logger logger = LoggerFactory.getLogger(ActivityReservationController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String bookingForm(Model model, @PathVariable String codeProvider, @PathVariable String codeActivity, @PathVariable String reference) {
		logger.info("reservationForm activityProviderCode:{}, activityCode:{}, offerReference:{}", codeProvider, codeActivity, reference);

		ActivityOfferData activityOfferData = ActivityInterface.getActivityOfferDataByReference(codeProvider, codeActivity, reference);

		if (activityOfferData == null) {
			model.addAttribute("error",
					"Error: it does not exist an offer with reference " + reference + " in activity with code " + codeActivity + " in activityProvider with code " + codeProvider);
			model.addAttribute("activityProvider", new ActivityProviderData());
			model.addAttribute("providers", ActivityInterface.getProviders());
			return "providers";
		} else {
			model.addAttribute("reservation", new ActivityReservationData());
			model.addAttribute("offer", activityOfferData);
			return "reservations";
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public String bookingSubmit(Model model, @PathVariable String codeProvider, @PathVariable String codeActivity, @PathVariable String reference,
			@ModelAttribute ActivityReservationData reservation) {
		logger.info("reservationSubmit activityProviderCode:{}, activityCode:{}, offerReference:{}, buyerNif:{}, buyerIban:{}", codeProvider, codeActivity, reference,
				reservation.getBuyerNif(), reservation.getBuyerIban());

		try {
			ActivityInterface.createActivityReservation(codeProvider, codeActivity, reference, reservation);
		} catch (ActivityException be) {
			model.addAttribute("error", "Error: it was not possible to reserve the activity");
			model.addAttribute("reservation", reservation);
			model.addAttribute("offer", ActivityInterface.getActivityOfferDataByReference(codeProvider, codeActivity, reference));
			return "reservations";
		}

		return "redirect:/providers/" + codeProvider + "/activities/" + codeActivity + "/offers/" + reference + "/reservations";
	}
}
