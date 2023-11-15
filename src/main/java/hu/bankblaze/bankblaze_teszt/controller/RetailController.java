package hu.bankblaze.bankblaze_teszt.controller;


import hu.bankblaze.bankblaze_teszt.model.Retail;
import hu.bankblaze.bankblaze_teszt.service.RetailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/retail")
public class RetailController {

    private RetailService retailService;

    @GetMapping()
    public String getAllRetail(Model model){
        model.addAttribute("retails",retailService.getAllRetail());
        return "showRetail";
    }

    @GetMapping("/{id}")
    public String getRetailById (Model model, @PathVariable Long id){
        Retail retail = retailService.getRetailById(id);
        model.addAttribute("retail",retail);
        return "retail";
    }
}
