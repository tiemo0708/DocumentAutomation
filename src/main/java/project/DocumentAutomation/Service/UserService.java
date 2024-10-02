package project.DocumentAutomation.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.DocumentAutomation.Repository.UniversityRepository;
import project.DocumentAutomation.Repository.UserRepository;
import project.DocumentAutomation.domain.University;
import project.DocumentAutomation.domain.User;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    @Lazy
    private final PasswordEncoder passwordEncoder;
    private UniversityRepository universityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
    }

    public User createUser(String username, String password, User.Role role) {
        String encodedPassword = passwordEncoder.encode(password);
        User newUser = User.builder()
                .username(username)
                .password(encodedPassword)
                .role(role)
                .build();
        return userRepository.save(newUser);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Transactional
    public University createUniversity(String universityName, String clubName, String establishedYear, String password,  User currentUser) {
        // 관리자 권한 확인 로직 필요
        checkAdminAuthority(currentUser);
        // 대학교 생성
        University university = University.builder()
                .universityName(universityName)
                .clubName(clubName)
                .establishedYear(establishedYear)
                .password(passwordEncoder.encode(password))
                .build();
        return universityRepository.save(university);
    }

    @Transactional
    public User createUniversityUser(String username, String password, String universityName) {
        University university = universityRepository.findById(universityName)
                .orElseThrow(() -> new IllegalArgumentException("University not found"));

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(User.Role.UNIVERSITY)
                .university(university)
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public boolean authenticateUniversity(String universityName, String password) {
        University university = universityRepository.findById(universityName)
                .orElseThrow(() -> new IllegalArgumentException("University not found"));

        return passwordEncoder.matches(password, university.getPassword());
    }
    public void checkAdminAuthority(User user) {
        if (user.getRole() != User.Role.ADMIN) {
            throw new AccessDeniedException("관리자 권한이 필요합니다.");
        }
    }
}