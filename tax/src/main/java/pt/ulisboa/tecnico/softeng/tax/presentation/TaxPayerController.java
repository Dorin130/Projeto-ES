package pt.ulisboa.tecnico.softeng.tax.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;
import pt.ulisboa.tecnico.softeng.tax.services.local.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.TaxPayerData;

import java.util.Map;

@Controller
@RequestMapping(value = "/taxpayers")
public class TaxPayerController {

    private static Logger logger = LoggerFactory.getLogger(TaxPayerController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String taxPayerForm(Model model) {
        logger.info("taxPayerForm");
        model.addAttribute("taxPayer", new TaxPayerData());
        model.addAttribute("taxPayers", TaxInterface.getTaxPayers());
        return "taxpayers";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/taxpayer/{nif}/taxes" )
    public String taxByYear(Model model, @PathVariable String nif) {
        logger.info("taxByYear");
        TaxPayerData taxPayer = TaxInterface.getTaxPayer(nif);

        if(taxPayer != null) {
            model.addAttribute("taxPayer", taxPayer);
            Map<Integer, Double> yearMap = TaxInterface.getTaxMap(nif);
            model.addAttribute("years", yearMap.keySet());
            model.addAttribute("yearMap", yearMap);
            return "taxesbyyear";
        }else {
            model.addAttribute("error", "Error: inexisting taxPayer with nif " + nif);
            model.addAttribute("taxPayer", new TaxPayerData());
            model.addAttribute("taxPayers", TaxInterface.getTaxPayers());
            return "taxpayers";
        }
    }


    @RequestMapping(method = RequestMethod.POST)
    public String taxPayerSubmit(Model model, @ModelAttribute TaxPayerData taxPayer) {
        logger.info("TaxPayerSubmit type:{} name:{}, nif:{}, address:{}", taxPayer.getType(),
                taxPayer.getName(), taxPayer.getNIF(), taxPayer.getAddress());

        try {
            if(taxPayer.getType().equals("Buyer")) {
                TaxInterface.createBuyer(taxPayer);
            } else if (taxPayer.getType().equals("Seller")) {
                TaxInterface.createSeller(taxPayer);
            } else {
                throw new TaxException();
            }
        } catch (TaxException be) {
            model.addAttribute("error", "Error: it was not possible to create the TaxPayer");
            model.addAttribute("taxPayer", new TaxPayerData());
            model.addAttribute("taxPayers", TaxInterface.getTaxPayers());
            return "taxpayers";
        }

        return "redirect:/taxpayers";
    }
}
