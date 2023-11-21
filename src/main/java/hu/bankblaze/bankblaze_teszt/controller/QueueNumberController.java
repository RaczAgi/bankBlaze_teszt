package hu.bankblaze.bankblaze_teszt.controller;


import hu.bankblaze.bankblaze_teszt.model.QueueNumber;
import hu.bankblaze.bankblaze_teszt.service.QueueNumberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@AllArgsConstructor
@RequestMapping("/queue")
public class QueueNumberController {

    private QueueNumberService queueNumberService;


    @GetMapping("/showNumber")
    public String showQueueNumberPage(Model model) {
        model.addAttribute("queueNumber", queueNumberService.getQueueNumber());
        return "showNumber";
    }

    @PostMapping("/showNumber")
    public String confirmQueueNumber(@RequestParam("action") String action) {
        if (action.equals("delete")) {
            queueNumberService.deleteQueueNumber();
        }
        return "redirect:/home";
    }


    @GetMapping("/queueNumber")
    public String queueQueueNumberPage() {
        return "queueNumber";
    }

    @PostMapping("/queueNumber")
    public String generateQueueNumber(@RequestParam String name) {
        queueNumberService.modifyName(name);
        return "redirect:/queue/showNumber";
    }

    @GetMapping("/delete/{id}")
    public String deleteQueueNumber(@PathVariable Long id) {
        queueNumberService.deleteQueueNumberById(id);
        return "redirect:/queue/showNumber";
    }

}