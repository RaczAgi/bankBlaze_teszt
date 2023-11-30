package hu.bankblaze.bankblaze_teszt.controller;

import hu.bankblaze.bankblaze_teszt.model.Employee;
import hu.bankblaze.bankblaze_teszt.model.Permission;
import hu.bankblaze.bankblaze_teszt.model.QueueNumber;
import hu.bankblaze.bankblaze_teszt.service.AdminService;
import hu.bankblaze.bankblaze_teszt.service.DeskService;
import hu.bankblaze.bankblaze_teszt.service.PermissionService;
import hu.bankblaze.bankblaze_teszt.service.QueueNumberService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        Employee employee = adminService.getEmployeeByName(adminService.getLoggedInUsername());
        model.addAttribute("desk", deskService.getDeskByEmployeeId(employee.getId()));

        adminService.getLoggedInClerks();
        adminService.getLoggedInUsername();
        adminService.setQueueCounts(model);
        adminService.setActualPermission(model, employee);
        adminService.setActualCount(model, employee);
        adminService.setEmployeeCount(model, employee);

            return "employee";
    }

    @PostMapping
    public String nextQueueNumber(Model model) {
        Employee employee = adminService.getEmployeeByName(adminService.getLoggedInUsername());
       deskService.nextQueueNumber(employee);
        adminService.setActualPermission(model, employee);
        return "redirect:/desk/next";
    }

    @GetMapping("/closure")
    public String getClosure(Model model){
        adminService.processClosure(model);
        return "redirect:/employee";
    }
    @GetMapping("/redirect")
    public String getRedirect(Model model, @RequestParam("redirectLocation") String redirectLocation) {
        adminService.processRedirect(model, redirectLocation);
        return "redirect:/employee";
    }

    @GetMapping("/deleteNumber")
    public String deleteNumber(Model model){
        adminService.deleteNextQueueNumber(model);
        return "redirect:/employee";
    }



}
