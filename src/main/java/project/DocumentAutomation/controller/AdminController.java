package project.DocumentAutomation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.DocumentAutomation.Service.UserService;
import project.DocumentAutomation.domain.User;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @PostMapping("/createUniversity")
    public String createUniversity(@RequestParam String username, @RequestParam String password) {
        userService.createUser(username, password, User.Role.UNIVERSITY);
        return "redirect:/admin/dashboard";
    }
}