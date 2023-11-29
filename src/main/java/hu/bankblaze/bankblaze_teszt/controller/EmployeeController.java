package hu.bankblaze.bankblaze_teszt.controller;

import hu.bankblaze.bankblaze_teszt.model.Desk;
import hu.bankblaze.bankblaze_teszt.model.Permission;
import hu.bankblaze.bankblaze_teszt.model.QueueNumber;
import hu.bankblaze.bankblaze_teszt.repo.PermissionRepository;
import hu.bankblaze.bankblaze_teszt.repo.QueueNumberRepository;
import hu.bankblaze.bankblaze_teszt.service.AdminService;
import hu.bankblaze.bankblaze_teszt.service.DeskService;
import hu.bankblaze.bankblaze_teszt.service.PermissionService;
import hu.bankblaze.bankblaze_teszt.service.QueueNumberService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {

    private AdminService adminService;
    private DeskService deskService;



    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public String getEmployeePage(Model model) {

        adminService.getLoggedInClerks(model);
        adminService.getLoggedInUsername();
        adminService.setQueueCounts(model);
        adminService.setActualCount(model);
        adminService.setEmployeeCount(model);


            return "employee";
    }

    @PostMapping
    public String assignDeskToEmployee(@RequestParam("employeeId") Long employeeId,
                                       @RequestParam("deskId") Long deskId,
                                       @RequestParam("queueNumberId") Long queueNumberId) {
        deskService.assignDeskAndQueueNumber(employeeId, deskId, queueNumberId);
        return "redirect:/employee";
    }

    @GetMapping("/closure")
    public String getClosure(Model model){
        adminService.processClosure(model);
        return "redirect:/employee";
    }
    @GetMapping("/redirect")
    public String getRedirect(Model model, @RequestParam("redirectLocation") String redirectLocation) {
        adminService.processRedirect(model, redirectLocation);
        return "employee";
    }

    @GetMapping("/deleteNumber")
    public String deleteNumber(Model model){
        adminService.deleteNextQueueNumber(model);
        return "employee";
    }



}
