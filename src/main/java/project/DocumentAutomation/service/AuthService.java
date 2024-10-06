package project.DocumentAutomation.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import project.DocumentAutomation.dto.LoginDto;
import project.DocumentAutomation.dto.TokenDto;
import project.DocumentAutomation.security.TokenProvider;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    public Authentication authenticateUser(LoginDto loginDto) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        return authenticationManager.authenticate(authenticationToken);
    }

    public String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public TokenDto generateToken(Authentication authentication) {
        return tokenProvider.generateTokenDto(authentication);
    }

    public TokenDto refreshToken(String refreshToken) {
        return tokenProvider.refreshToken(refreshToken);
    }
}