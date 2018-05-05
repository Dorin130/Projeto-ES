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

import java.util.List;


@Controller
@RequestMapping(value = "/taxpayers/taxpayer/{NIF}")
public class InvoiceController {

    private static Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    @RequestMapping(value = "/invoicesSeller", method = RequestMethod.GET)
    public String invoiceSellerForm(Model model, @PathVariable String NIF) {
        logger.info("invoiceForm");
        
        TaxPayerData taxPayerData = TaxInterface.getTaxPayer(NIF);
        InvoiceData invoiceData = new InvoiceData();
        
        invoiceData.setSellerNIF(NIF);
        
        if (taxPayerData == null) { 
			model.addAttribute("error", "Error: it does not exist a TaxPayer with the nif " + NIF);
			model.addAttribute("taxPayer", new TaxPayerData());
	        model.addAttribute("taxPayers", TaxInterface.getTaxPayers());
			return "taxpayers";
		} 
        else if (!taxPayerData.getType().equals("Seller")) {
        	model.addAttribute("error", "Error: the nif " + NIF + " does not belong to a Seller");
        	model.addAttribute("taxPayer", taxPayerData);
        	model.addAttribute("taxPayers", TaxInterface.getTaxPayers());
        	return "taxpayers";
        }
        else {
			model.addAttribute("invoice", invoiceData);
			model.addAttribute("taxPayer", taxPayerData);
			model.addAttribute("invoices", TaxInterface.getInvoicesData(taxPayerData));
			return "invoicesSeller";
		}
    }
    
    @RequestMapping(value = "/invoicesBuyer", method = RequestMethod.GET)
    public String invoiceBuyerForm(Model model, @PathVariable String NIF) {
        logger.info("invoiceForm");
        
        TaxPayerData taxPayerData = TaxInterface.getTaxPayer(NIF);
        InvoiceData invoiceData = new InvoiceData();
        
        invoiceData.setBuyerNIF(NIF);
        
        if (taxPayerData == null) { 
			model.addAttribute("error", "Error: it does not exist a TaxPayer with the nif " + NIF);
			model.addAttribute("taxPayer", new TaxPayerData());
	        model.addAttribute("taxPayers", TaxInterface.getTaxPayers());
			return "taxpayers";
		}
        else if (!taxPayerData.getType().equals("Buyer")) {
        	model.addAttribute("error", "Error: the nif " + NIF + " does not belong to a Buyer");
        	model.addAttribute("taxPayer", taxPayerData);
        	model.addAttribute("taxPayers", TaxInterface.getTaxPayers());
        	return "taxpayers";
        }
        else {
			model.addAttribute("invoice", invoiceData);
			model.addAttribute("taxPayer", taxPayerData);
			model.addAttribute("invoices", TaxInterface.getInvoicesData(taxPayerData));
			return "invoicesBuyer";
		}
    }

    @RequestMapping(value = "/invoicesSeller", method = RequestMethod.POST)
    public String sellerSubmit(Model model, @ModelAttribute TaxPayerData taxPayerData, @ModelAttribute InvoiceData invoiceData, @PathVariable String NIF) {
    	logger.info("InvoiceSubmit sellerNif:{}, buyerNif:{}, itemType:{}, value:{}, date:{}", invoiceData.getSellerNIF(),
                invoiceData.getBuyerNIF(), invoiceData.getItemType(), invoiceData.getValue(), invoiceData.getDate());
    	String buyerNif = invoiceData.getBuyerNIF();
    	
    	TaxPayerData taxPayerDataBuyer = TaxInterface.getTaxPayer(buyerNif);
    	if (taxPayerDataBuyer != null && !taxPayerDataBuyer.getType().equals("Buyer")) {
    		model.addAttribute("error", "Error: inexisting Buyer with nif " + buyerNif);
    		model.addAttribute("taxPayer", taxPayerData);
    		model.addAttribute("taxPayers", TaxInterface.getTaxPayers());
    		model.addAttribute("invoice", invoiceData);
    		model.addAttribute("invoices", TaxInterface.getInvoicesData(TaxInterface.getTaxPayer(NIF)));
    		return "taxpayers";
    	}
        try {
        	TaxInterface.submitInvoice(invoiceData);
        } catch (TaxException be) {
            model.addAttribute("error", "Error: it was not possible to create the Invoice");
            model.addAttribute("taxPayer", taxPayerData);
            model.addAttribute("invoice", invoiceData);
			taxPayerData.setType("Seller");
            model.addAttribute("invoices", TaxInterface.getInvoicesData(taxPayerData));
            return "invoicesSeller";
        }

        return "redirect:/taxpayers/taxpayer/{NIF}/invoicesSeller";
    }
    
    @RequestMapping(value = "/invoicesBuyer", method = RequestMethod.POST)
    public String buyerSubmit(Model model, @ModelAttribute TaxPayerData taxPayerData, @ModelAttribute InvoiceData invoiceData, @PathVariable String NIF) {
    	logger.info("InvoiceSubmit sellerNif:{}, buyerNif:{}, itemType:{}, value:{}, date:{}", invoiceData.getSellerNIF(),
                invoiceData.getBuyerNIF(), invoiceData.getItemType(), invoiceData.getValue(), invoiceData.getDate());
    	String sellerNif = invoiceData.getSellerNIF();
    	
    	TaxPayerData taxPayerDataSeller = TaxInterface.getTaxPayer(sellerNif);
    	if (taxPayerDataSeller != null && !taxPayerDataSeller.getType().equals("Seller")) {
    		model.addAttribute("error", "Error: inexisting Seller with nif " + sellerNif);
    		model.addAttribute("taxPayer", taxPayerData);
    		model.addAttribute("taxPayers", TaxInterface.getTaxPayers());
    		model.addAttribute("invoice", invoiceData);
    		model.addAttribute("invoices", TaxInterface.getInvoicesData(TaxInterface.getTaxPayer(NIF)));
    		return "taxpayers";
    	}
        try {
        	TaxInterface.submitInvoice(invoiceData);
        } catch (TaxException be) {
            model.addAttribute("error", "Error: it was not possible to create the Invoice");
            model.addAttribute("taxPayer", taxPayerData);
            model.addAttribute("invoice", invoiceData);
			taxPayerData.setType("Buyer");
            model.addAttribute("invoices", TaxInterface.getInvoicesData(taxPayerData));
            return "invoicesBuyer";
        }

        return "redirect:/taxpayers/taxpayer/{NIF}/invoicesBuyer";
    }
}
