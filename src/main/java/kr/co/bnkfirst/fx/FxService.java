package kr.co.bnkfirst.fx;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FxService {

    private final KoreaEximFxClient fxClient;   // 우리가 이미 만든 Client
    private final ObjectMapper objectMapper;    // 스프링이 자동으로 Bean 등록해줌

    /**
     * 오늘 기준 USD/KRW 환율 가져오기
     */
    public double getUsdKrwRateToday() {
        return getUsdKrwRate(LocalDate.now());
    }

    /**
     * 특정 날짜 기준 USD/KRW 환율
     * (토/일이면 자동으로 직전 금요일로 보정)
     */
    public double getUsdKrwRate(LocalDate date) {
        LocalDate targetDate = adjustToBusinessDay(date);

        String json = fxClient.getRatesRaw(targetDate);

        try {
            JsonNode arr = objectMapper.readTree(json);

            if (!arr.isArray() || arr.size() == 0) {
                throw new IllegalStateException("환율 데이터가 비어 있습니다. date=" + targetDate);
            }

            for (JsonNode node : arr) {
                String curUnit = node.path("cur_unit").asText();
                if ("USD".equals(curUnit)) {
                    String dealBasR = node.path("deal_bas_r").asText(); // "1,461.99" 형식
                    dealBasR = dealBasR.replace(",", "");
                    return Double.parseDouble(dealBasR);
                }
            }

            throw new IllegalStateException("USD 환율 항목을 찾지 못했습니다. date=" + targetDate);

        } catch (IOException e) {
            throw new RuntimeException("환율 응답(JSON) 파싱 실패", e);
        }
    }

    /**
     * 토/일이면 직전 금요일로 돌리는 간단 보정
     */
    private LocalDate adjustToBusinessDay(LocalDate date) {
        DayOfWeek dow = date.getDayOfWeek();
        if (dow == DayOfWeek.SATURDAY) {
            return date.minusDays(1);
        } else if (dow == DayOfWeek.SUNDAY) {
            return date.minusDays(2);
        }
        return date;
    }
}
