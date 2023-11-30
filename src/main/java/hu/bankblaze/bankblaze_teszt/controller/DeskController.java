package hu.bankblaze.bankblaze_teszt.controller;

import hu.bankblaze.bankblaze_teszt.model.Desk;
import hu.bankblaze.bankblaze_teszt.model.Employee;
import hu.bankblaze.bankblaze_teszt.model.Permission;
import hu.bankblaze.bankblaze_teszt.model.QueueNumber;
import hu.bankblaze.bankblaze_teszt.repo.DeskRepository;
import hu.bankblaze.bankblaze_teszt.repo.EmployeeRepository;
import hu.bankblaze.bankblaze_teszt.repo.PermissionRepository;
import hu.bankblaze.bankblaze_teszt.repo.QueueNumberRepository;
import hu.bankblaze.bankblaze_teszt.service.AdminService;
import hu.bankblaze.bankblaze_teszt.service.DeskService;
import hu.bankblaze.bankblaze_teszt.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/desk")
public class DeskController {
    public QueueNumberRepository queueNumberRepository;
    public DeskRepository deskRepository;
    public DeskService deskService;
    public EmployeeRepository employeeRepository;
    public AdminService adminService;
    public PermissionService permissionService;
    public PermissionRepository permissionRepository;

    @GetMapping("/next")
    public String getNextClient(Model model) {
        Employee employee = adminService.getEmployeeByName(adminService.getLoggedInUsername());
        model.addAttribute("desk", deskService.getDeskByEmployeeId(employee.getId()));
        adminService.setQueueCounts(model);
        adminService.setActualPermission(model, employee);
        adminService.setActualCount(model, employee);
        adminService.setEmployeeCount(model, employee);
        return "next";
    }

    @PostMapping("next")
    public String getNextClient(){
        return "redirect:/next";
    }

}
