package hu.bankblaze.bankblaze_teszt.controller;


import hu.bankblaze.bankblaze_teszt.model.Teller;
import hu.bankblaze.bankblaze_teszt.service.TellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/tellers")
public class TellerController {

    private final TellerService tellerService;

    @Autowired
    public TellerController(TellerService tellerService) {
        this.tellerService = tellerService;
    }

    @GetMapping
    public String showTellers(Model model) {
        List<Teller> tellers = tellerService.getAllTellers();
        model.addAttribute("tellers", tellers);
        return "showTeller";
    }

    @GetMapping("/queueNumber")
    public String queueNumberPage() {
        return "queueNumber";
    }
}