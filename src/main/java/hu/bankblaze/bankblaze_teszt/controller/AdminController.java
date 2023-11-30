package hu.bankblaze.bankblaze_teszt.controller;
import hu.bankblaze.bankblaze_teszt.model.Employee;
import hu.bankblaze.bankblaze_teszt.model.Permission;
import hu.bankblaze.bankblaze_teszt.repo.EmployeeRepository;
import hu.bankblaze.bankblaze_teszt.repo.QueueNumberRepository;
import hu.bankblaze.bankblaze_teszt.service.AdminService;
import hu.bankblaze.bankblaze_teszt.service.PermissionService;
import hu.bankblaze.bankblaze_teszt.service.QueueNumberService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private AdminService adminService;
    private QueueNumberService queueNumberService;
    private PermissionService permissionService;


    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getAllClerks(Model model) {
        model.addAttribute("employees", adminService.getAllClerks());
        model.addAttribute("admins", adminService.getAllAdmins());
        model.addAttribute("permissions", permissionService.getAllPermissions());
        return "admin";
    }

    @PostMapping("/update")
    public String updatePermissions(@RequestParam Map<String, String> permissions) {
        permissionService.updatePermissions(permissions);
        return "redirect:/admin";
    }

    @GetMapping("/statistics")
    public String getStatistics(Model model) {
        queueNumberService.getStatistics(model);
        return "statistics";
    }

    @GetMapping("/desk")
    public String setDesks(Model model) {
        model.addAttribute("admins", adminService.getAllAdmins());
        return "desk";
    }

    @PostMapping("/desk")
    public String setDesks(@RequestParam("employeeId") Long id) {
        return "redirect:/admin/desk";
    }

    @GetMapping("/registration")
    public String createEmployee(Model model) {
        model.addAttribute("admins", adminService.getAllAdmins());
        model.addAttribute("newEmployee", new Employee());
        return "registration";
    }

    @PostMapping("/registration")
    public String createEmployee(@ModelAttribute("newEmployee") Employee employee,
                                 @RequestParam("defaultRole") String defaultRole) {
        employee.setRole(String.valueOf(defaultRole));
        adminService.saveAdmin(employee);
        return "redirect:/admin";
    }
    @GetMapping("/delete")
    public String showDeleteForm(Model model) {
        model.addAttribute("admins", adminService.getAllAdmins());
        model.addAttribute("admins", adminService.getAllClerks());
        return "delete";
    }
    @PostMapping("/delete")
    public String deleteAdmin(@RequestParam("action") String action, String name) {
        if (action.equals("delete")) {
            adminService.deleteAdminByName(name);
        }
        return "redirect:/admin";
    }


    @PostMapping("update/{id}")
    public String updatePermission(@PathVariable("id") Integer id, @ModelAttribute("permission") Permission update) {
        permissionService.savePermisson(update);
        return "redirect:/admin/" + id;
    }


}