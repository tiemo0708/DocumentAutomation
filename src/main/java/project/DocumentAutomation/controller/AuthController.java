package project.DocumentAutomation.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.DocumentAutomation.Repository.RefreshTokenRepository;
import project.DocumentAutomation.domain.RefreshToken;
import project.DocumentAutomation.dto.AccessTokenResponse;
import project.DocumentAutomation.dto.LoginDto;
import project.DocumentAutomation.dto.TokenDto;
import project.DocumentAutomation.dto.TokenRequestDto;
import project.DocumentAutomation.exception.InvalidRefreshTokenException;
import project.DocumentAutomation.security.TokenProvider;

import java.time.Duration;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        try {
            // 인증 토큰 생성 및 인증 수행
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // JWT 토큰 생성
            TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

            // Refresh Token을 HttpOnly 쿠키에 저장
            ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", tokenDto.getRefreshToken())
                    .httpOnly(true)
                    .secure(true) // HTTPS를 사용하는 경우에만 true로 설정
                    .sameSite("Strict")
                    .maxAge(Duration.ofDays(7)) // Refresh Token의 유효기간
                    .path("/api/auth/refresh") // Refresh 요청 경로에만 쿠키 전송
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
    public ResponseEntity<?> reissueToken(@Valid @RequestBody TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new InvalidRefreshTokenException("Refresh Token이 유효하지 않습니다.");
        }

        // 2. Access Token에서 Member ID 추출
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return ResponseEntity.ok(tokenDto);
    }
}