package project.DocumentAutomation.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DashboardController {

    @GetMapping("/api/dashboard")
    public String dashboard(Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "admin_dashboard";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_UNIVERSITY"))) {
            return "university_dashboard";
        }
        return "access_denied";
    }
}