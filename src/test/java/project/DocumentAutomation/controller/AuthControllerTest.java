import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import project.DocumentAutomation.DocumentAutomationApplication;
import project.DocumentAutomation.dto.LoginDto;
import project.DocumentAutomation.dto.TokenDto;
import project.DocumentAutomation.security.TokenProvider;

import java.util.Collection;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = DocumentAutomationApplication.class)
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // @MockBean을 사용하여 AuthenticationManager와 TokenProvider를 모킹
    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private TokenProvider tokenProvider;

    private LoginDto loginDto;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        // 로그인 정보 초기화
        loginDto = new LoginDto();
        loginDto.setUsername("admin");
        loginDto.setPassword("adminPassword");

        // Mock Authentication 객체 생성
        authentication = Mockito.mock(Authentication.class);

        // 권한 목록 설정
        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));

        // getAuthorities()가 권한 목록을 반환하도록 설정 (명시적 캐스팅 추가)
        Mockito.when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        // isAuthenticated()가 true를 반환하도록 설정
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);

        // AuthenticationManager가 authentication 객체를 반환하도록 설정
        Mockito.when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(authentication);

        // TokenProvider가 토큰을 생성할 때 반환할 TokenDto 설정
        TokenDto tokenDto = new TokenDto("Bearer", "access-token-123", "refresh-token-456", 3600L);
        Mockito.when(tokenProvider.generateTokenDto(any(Authentication.class))).thenReturn(tokenDto);
    }

    //정상적인 로그인 시, Access Token과 Refresh Token이 제대로 생성되고 반환되는지
    @Test
    void authenticate_shouldReturnAccessToken() throws Exception {
        // TokenProvider가 토큰을 생성할 때 반환할 TokenDto 설정
        TokenDto tokenDto = new TokenDto("Bearer", "access-token-123", "refresh-token-456", 3600L);

        // TokenProvider의 generateTokenDto 메서드가 호출될 때, 미리 설정한 tokenDto 반환
        Mockito.when(tokenProvider.generateTokenDto(any(Authentication.class))).thenReturn(tokenDto);

        // 로그인 요청을 보내고, 응답 상태 코드와 Access Token을 확인
        ResultActions resultActions = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))  // 로그인 정보로 "admin"과 "adminPassword" 사용
                .andExpect(status().isOk());

        // Access Token이 응답에 포함되었는지 확인
        resultActions.andExpect(result -> {
            String responseContent = result.getResponse().getContentAsString();
            assert responseContent.contains("access-token-123");
        });

        // Refresh Token이 쿠키에 설정되었는지 확인
        resultActions.andExpect(cookie().exists("refresh_token"));
        resultActions.andExpect(cookie().httpOnly("refresh_token", true));
    }

    //유효한 Refresh Token을 사용해 새로운 Access Token을 발급받는 과정을 확인
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void refreshToken_shouldReturnNewAccessToken() throws Exception {
        // TokenProvider가 refreshToken 메서드에서 반환할 TokenDto 설정
        TokenDto newTokenDto = new TokenDto("Bearer", "new-access-token-123", "new-refresh-token-456", 3600L);

        // TokenProvider의 refreshToken 메서드가 호출될 때, 미리 설정한 newTokenDto 반환
        Mockito.when(tokenProvider.refreshToken(any(String.class))).thenReturn(newTokenDto);

        // Refresh Token을 쿠키에 넣고 요청을 보냄
        ResultActions resultActions = mockMvc.perform(post("/api/auth/reissue")
                        .cookie(new Cookie("refresh_token", "refresh-token-456")))
                .andExpect(status().isOk());

        // 새로 발급된 Access Token이 응답에 포함되었는지 확인
        resultActions.andExpect(result -> {
            String responseContent = result.getResponse().getContentAsString();
            assert responseContent.contains("new-access-token-123");
        });

        // 새로 발급된 Refresh Token이 쿠키에 설정되었는지 확인
        resultActions.andExpect(cookie().value("refresh_token", "new-refresh-token-456"));
        resultActions.andExpect(cookie().httpOnly("refresh_token", true));
    }

    @Test
    void authenticate_shouldFailWithInvalidCredentials() throws Exception {
        // AuthenticationManager가 인증 실패하도록 설정 (AuthenticationException 사용)
        Mockito.doThrow(new org.springframework.security.core.AuthenticationException("Invalid credentials") {}).when(authenticationManager).authenticate(any());

        // 잘못된 로그인 정보로 요청을 보낸 후, 인증 실패 상태 코드(401)를 확인
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))  // 잘못된 로그인 정보로 요청
                .andExpect(status().isUnauthorized());
    }

}
