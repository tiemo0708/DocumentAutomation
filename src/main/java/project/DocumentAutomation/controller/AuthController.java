package project.DocumentAutomation.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.DocumentAutomation.dto.AccessTokenResponse;
import project.DocumentAutomation.dto.LoginDto;
import project.DocumentAutomation.dto.TokenDto;
import project.DocumentAutomation.Service.AuthService;
import java.time.Duration;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        try {
            Authentication authentication = authService.authenticateUser(loginDto);
            TokenDto tokenDto = authService.generateToken(authentication);

            // Refresh Token을 HttpOnly 쿠키에 저장
            ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", tokenDto.getRefreshToken())
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .maxAge(Duration.ofDays(7))
                    .path("/api/auth/reissue")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

            // Access Token만 반환 (클라이언트에서 메모리에 저장)
            return ResponseEntity.ok(new AccessTokenResponse(tokenDto.getAccessToken()));
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", loginDto.getUsername(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        } catch (Exception e) {
            log.error("Login failed for user: {}", loginDto.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = authService.extractRefreshTokenFromCookie(request);
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token is missing");
        }

        try {
            TokenDto newTokenDto = authService.refreshToken(refreshToken);

            // 새 Refresh Token을 쿠키에 설정
            ResponseCookie newRefreshTokenCookie = ResponseCookie.from("refresh_token", newTokenDto.getRefreshToken())
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .maxAge(Duration.ofDays(7))
                    .path("/api/auth/reissue")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, newRefreshTokenCookie.toString());

            // 새 Access Token 반환
            return ResponseEntity.ok(new AccessTokenResponse(newTokenDto.getAccessToken()));
        } catch (Exception e) {
            log.error("Token refresh failed", e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token refresh failed: " + e.getMessage());
        }
    }
}