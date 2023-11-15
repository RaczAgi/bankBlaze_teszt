package hu.bankblaze.bankblaze_teszt.controller;

import hu.bankblaze.bankblaze_teszt.model.Employee;
import hu.bankblaze.bankblaze_teszt.model.SecurityUser;
import hu.bankblaze.bankblaze_teszt.repo.EmployeeRepository;
import hu.bankblaze.bankblaze_teszt.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@Controller
@AllArgsConstructor
public abstract class PageController implements AuthenticationManager {

private UserDetailsService userDetailsService;


    @GetMapping("/home")
    public String goHome() {
        return "home";
    }


    @GetMapping("/admin")
    public String showLogin(){
        return "login";
    }

    @PostMapping("/admin")
    public String processLogin(Authentication authentication, @RequestParam String username, @RequestParam String password) {
        if (authentication != null && authentication.isAuthenticated()) {
            // Felhasználó már be van jelentkezve
            return "redirect:/admin";
        } else {
            // Felhasználói adatok ellenőrzése és bejelentkeztetés
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);// lekérni a JpaUserDetailsService-ből a felhasználó adatait a username alapján
            if (userDetails != null && userDetails.getPassword().equals(password)) {
                return "redirect:/admin";
            } else {
                return "redirect:/login?error=true";
            }
        }
    }
}