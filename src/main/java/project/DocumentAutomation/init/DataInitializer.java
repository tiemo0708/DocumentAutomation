package project.DocumentAutomation.init;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import project.DocumentAutomation.service.UserService;
import project.DocumentAutomation.domain.User;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;

    //외부 노출 안되도록 수정할 것
    @Override
    public void run(String... args) {
        if (userService.findByUsername("admin").isEmpty()) {
            userService.createUser("admin", "adminPassword", User.Role.ADMIN);
        }
    }
}