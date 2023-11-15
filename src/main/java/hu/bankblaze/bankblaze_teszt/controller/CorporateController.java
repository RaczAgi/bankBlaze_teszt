package hu.bankblaze.bankblaze_teszt.controller;


import hu.bankblaze.bankblaze_teszt.service.CorporateService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/corporate")
public class CorporateController {

    private CorporateService corporateService;

    @GetMapping
    public String getCorporate(Model model) {
        model.addAttribute("corporates", corporateService.getAllCorporates());
        return "showCorporate";
    }
}
