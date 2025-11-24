package kr.co.bnkfirst.fx;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class KoreaEximFxClient {

    private final RestTemplate restTemplate;

    @Value("${koreaexim.base-url}")
    private String baseUrl;

    @Value("${koreaexim.auth-key}")
    private String authKey;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.BASIC_ISO_DATE; // yyyyMMdd

    /**
     * 수출입은행 현재환율 API raw JSON 문자열 그대로 반환
     */
    public String getRatesRaw(LocalDate date) {
        String searchDate = date.format(DATE_FMT);

        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("authkey", authKey)
                .queryParam("searchdate", searchDate)
                .queryParam("data", "AP01")   // AP01 = 현재환율
                .toUriString();

        return restTemplate.getForObject(url, String.class);
    }
}
