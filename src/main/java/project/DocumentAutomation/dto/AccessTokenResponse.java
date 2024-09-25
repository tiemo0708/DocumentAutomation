package project.DocumentAutomation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenResponse {
    private String accessToken;
    private long expiresIn; // 토큰 만료 시간 (초 단위)

    // 선택적: 토큰 타입 추가 (대부분의 경우 "Bearer"로 고정)
    private String tokenType = "Bearer";

    // Access Token만 받는 생성자
    public AccessTokenResponse(String accessToken) {
        this.accessToken = accessToken;
        this.tokenType = "Bearer";
    }

    // Access Token과 만료 시간을 받는 생성자
    public AccessTokenResponse(String accessToken, long expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.tokenType = "Bearer";
    }
}