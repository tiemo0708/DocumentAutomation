package project.DocumentAutomation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenRefreshRequest {

    @NotBlank(message = "Refresh token is required")
    private String refreshToken;

    @Override
    public String toString() {
        return "TokenRefreshRequest{" +
                "refreshToken='" + refreshToken.substring(0, Math.min(refreshToken.length(), 6)) + "..." + '\'' +
                '}';
    }
}