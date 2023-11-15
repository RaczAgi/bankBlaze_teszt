package hu.bankblaze.bankblaze_teszt.controller;


import hu.bankblaze.bankblaze_teszt.model.Premium;
import hu.bankblaze.bankblaze_teszt.model.QueueNumber;
import hu.bankblaze.bankblaze_teszt.service.PremiumService;
import hu.bankblaze.bankblaze_teszt.service.QueueNumberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/premium")
public class PremiumController {

    private PremiumService premiumService;
    private QueueNumberService queueNumberService;

    @GetMapping
    public String getPremium (Model model) {
        model.addAttribute("header", "Prémium");
        model.addAttribute("newQueueNumber", new QueueNumber());
        return "queueNumber";
    }
    @PostMapping("/processPremium")
    public String processPremium(@ModelAttribute("Prémium") Premium premium, Model model, @ModelAttribute("newQueueNumber") QueueNumber newQueueNumber) {
        model.addAttribute("header", "Prémium");
        queueNumberService.generateQueueNumber(newQueueNumber);
        return "redirect:/queue/queueNumber";
    }


}

