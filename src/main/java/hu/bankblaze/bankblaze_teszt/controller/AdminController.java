package hu.bankblaze.bankblaze_teszt.controller;


import hu.bankblaze.bankblaze_teszt.model.Employee;
import hu.bankblaze.bankblaze_teszt.repo.EmployeeRepository;
import hu.bankblaze.bankblaze_teszt.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private AdminService adminService;


    @GetMapping
  //  @PreAuthorize("hasAuthority('ADMIN')")
    public String getAdmin(){
        return "admin";
    }

    @GetMapping("/{id}")
    public String getAdminById (Model model, @PathVariable Long id) {
        Employee employee = adminService.getAdminById(id);
        model.addAttribute("admin",employee);
        return "admin";

    }

    @GetMapping("/new")
    public String createEmployee (Model model) {
        model.addAttribute("admin", new Employee());
        return "newEmployee";
    }

    @PostMapping("/new")
    public String createEmployee (@RequestBody Employee employee ){
        adminService.saveAdmin(employee);
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteAdmin (@PathVariable Long id){
        adminService.deleteAdminById(id);
        return "redirect:/admin";
    }



}

