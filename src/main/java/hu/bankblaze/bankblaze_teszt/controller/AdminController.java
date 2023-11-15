package hu.bankblaze.bankblaze_teszt.controller;


import hu.bankblaze.bankblaze_teszt.model.Employee;
import hu.bankblaze.bankblaze_teszt.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private AdminService adminService;

    @GetMapping
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

