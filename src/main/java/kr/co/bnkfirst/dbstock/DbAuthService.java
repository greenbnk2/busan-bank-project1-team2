package kr.co.bnkfirst.dbstock;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DbAuthService {

    private final DbAuthProperties props;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private String accessToken;
    private LocalDateTime expiresAt;

    /** 항상 최신 access token 리턴 */
    public synchronized String getAccessToken() {

        // 기존 토큰이 살아있으면 그대로 사용
        if (accessToken != null && !isExpired()) {
            return accessToken;
        }

        // 만료되었거나 null이면 재발급
        refreshToken();
        return accessToken;
    }

    private boolean isExpired() {
        if (expiresAt == null) return true;
        return expiresAt.isBefore(LocalDateTime.now().plusSeconds(30));
    }

    private void refreshToken() {
        try {
            System.out.println("🔄 DBSEC 토큰 갱신 시도 중...");

            String body =
                    "grant_type=client_credentials" +
                            "&appkey=" + URLEncoder.encode(props.getAppkey(), StandardCharsets.UTF_8) +
                            "&appsecretkey=" + URLEncoder.encode(props.getSecret(), StandardCharsets.UTF_8) +
                            "&scope=" + URLEncoder.encode(props.getScope(), StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(props.getTokenUrl()))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // 실패했다면 기존 토큰 유지하도록 함
            if (response.statusCode() != 200) {
                System.err.println("❌ 토큰 발급 실패 (기존 토큰 유지): " + response.body());
                if (accessToken == null) {
                    // 첫 발급부터 실패한 경우 → 치명적 오류
                    throw new RuntimeException("초기 토큰 발급 실패: " + response.body());
                }
                return;
            }

            JsonNode json = objectMapper.readTree(response.body());

            this.accessToken = json.get("access_token").asText();
            long expiresIn = json.get("expires_in").asLong(); // 초 단위

            this.expiresAt = LocalDateTime.now().plusSeconds(expiresIn);

            System.out.println("🎉 DBSEC Token 발급 완료 (expires_in=" + expiresIn + ")");

        } catch (Exception ex) {
            System.err.println("❌ DBSEC Token Refresh Error: " + ex.getMessage());

            // 기존 토큰이 있으면 그대로 사용
            if (accessToken != null) {
                System.err.println("⚠️ 기존 토큰 유지하고 진행함");
                return;
            }

            // 기존 토큰도 없으면 더 이상 진행 불가 → 예외 던짐
            throw new RuntimeException("DBSEC Token 초기화 실패 (토큰 없음)", ex);
        }
    }
}
