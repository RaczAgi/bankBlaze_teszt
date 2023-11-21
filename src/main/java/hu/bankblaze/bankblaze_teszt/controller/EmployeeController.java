package hu.bankblaze.bankblaze_teszt.controller;

import hu.bankblaze.bankblaze_teszt.model.Desk;
import hu.bankblaze.bankblaze_teszt.repo.PermissionRepository;
import hu.bankblaze.bankblaze_teszt.repo.QueueNumberRepository;
import hu.bankblaze.bankblaze_teszt.service.AdminService;
import hu.bankblaze.bankblaze_teszt.service.DeskService;
import hu.bankblaze.bankblaze_teszt.service.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {

    private AdminService adminService;
    private QueueNumberRepository queueNumberRepository;
    private PermissionService permissionService;
    private DeskService deskService;
    private PermissionRepository permissionRepository;


    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public String getLoggedInClerks(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();
        model.addAttribute("loggedInUsername", loggedInUsername);
        Long loggedInUserId = adminService.getLoggedInUserIdByUsername(loggedInUsername);
        Long deskNumber = deskService.getDeskIdByLoggedInUser(loggedInUserId);
        model.addAttribute("deskNumber", deskNumber);
        model.addAttribute("lakossagCount", queueNumberRepository.countByNumberBetween(1000, 1999));
        model.addAttribute("vallalatCount", queueNumberRepository.countByNumberBetween(2000, 2999));
        model.addAttribute("penztarCount", queueNumberRepository.countByNumberBetween(3000, 3999));
        model.addAttribute("premiumCount", queueNumberRepository.countByNumberBetween(9000, 9999));
        String permission = permissionService.getPermissionByLoggedInUser(loggedInUserId);
        model.addAttribute("permission", permission);
        Integer actualCount = 0;
        if (permission.equals("Lakosság")) {
            actualCount = queueNumberRepository.countByNumberBetween(1000, 1999);
        } else if (permission.equals("Vállalat")) {
            actualCount = queueNumberRepository.countByNumberBetween(2000, 2999);
        } else if (permission.equals("Pénztár")) {
            actualCount = queueNumberRepository.countByNumberBetween(3000, 3999);
        } else if (permission.equals("Prémium")) {
            actualCount = queueNumberRepository.countByNumberBetween(9000, 9999);
        }
        model.addAttribute("actualCount", actualCount);
        Integer letszamCount = 0;
        if (permission.equals("Lakosság")) {
             letszamCount = permissionRepository.countByForRetailTrue();
        } else if (permission.equals("Vállalat")) {
             letszamCount = permissionRepository.countByForCorporateTrue();
        } else if (permission.equals("Pénztár")) {
             letszamCount = permissionRepository.countByForTellerTrue();
        } else if (permission.equals("Prémium")) {
            letszamCount = permissionRepository.countByForPremiumTrue();
        }
        model.addAttribute("letszamCount", letszamCount);
        return "employee";
    }
    @PostMapping
    public String getLoggedInClerks() {
        return "redirect:/employee";
    }

}
