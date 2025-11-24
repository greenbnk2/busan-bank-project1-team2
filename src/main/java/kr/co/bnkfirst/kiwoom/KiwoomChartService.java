// src/main/java/kr/co/bnkfirst/kiwoom/KiwoomChartService.java
package kr.co.bnkfirst.kiwoom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KiwoomChartService {

    private final KiwoomTrClient trClient;

    // code+interval 캐시
    private final Map<String, List<CandleDTO>> cache = new ConcurrentHashMap<>();

    public void ensureHistoryLoaded(String code, String interval, int limit) {
        String key = cacheKey(code, interval);
        List<CandleDTO> existing = cache.get(key);
        if (existing != null && !existing.isEmpty()) return;

        List<CandleDTO> candles = loadHistoryFromKiwoom(code, interval, limit);
        cache.put(key, candles);
    }

    public List<CandleDTO> getCandles(String code, String interval) {
        return cache.getOrDefault(cacheKey(code, interval), List.of());
    }

    private String cacheKey(String code, String interval) {
        return code + "_" + interval;
    }

    private List<CandleDTO> loadHistoryFromKiwoom(String code,
                                                  String interval,
                                                  int limit) {

        // ka10080 : 분봉 차트
        String trCode = "ka10080";

        Map<String, String> input = new HashMap<>();
        input.put("stk_cd", code);                 // 종목코드
        input.put("tic_scope", toTicScope(interval)); // "1m" -> "1"
        input.put("upd_stkpc_tp", "1");           // 수정주가

        List<Map<String, String>> rows =
                trClient.call(trCode, "stk_min_pole_chart_qry", input, limit);

        List<CandleDTO> list = new ArrayList<>();
        for (Map<String, String> r : rows) {
            CandleDTO c = toCandleDTO(r);
            if (c != null) list.add(c);
        }

        // 오래된 봉부터 정렬
        list.sort(Comparator.comparingLong(CandleDTO::getTime));
        return list;
    }

    private String toTicScope(String interval) {
        // "1m", "3m", ... -> "1", "3"
        if (interval.endsWith("m")) {
            return interval.substring(0, interval.length() - 1);
        }
        throw new IllegalArgumentException("지원하지 않는 interval: " + interval);
    }

    private CandleDTO toCandleDTO(Map<String, String> row) {
        try {
            // 🔹 시간: "20250917132000"
            String cntrTm = row.get("cntr_tm"); // yyyyMMddHHmmss
            long epochSec = parseCntrTimeToEpoch(cntrTm);

            // 🔹 가격: 앞에 '-' 붙어 있어도 절대값으로 사용
            long open  = parseKiwoomAbs(row.get("open_pric"));
            long high  = parseKiwoomAbs(row.get("high_pric"));
            long low   = parseKiwoomAbs(row.get("low_pric"));
            long close = parseKiwoomAbs(row.get("cur_prc"));

            return new CandleDTO(epochSec, open, high, low, close);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private long parseKiwoomAbs(String s) {
        if (s == null) return 0L;
        String cleaned = s.trim().replace(",", "");
        long v = Long.parseLong(cleaned);
        return Math.abs(v);
    }

    private long parseCntrTimeToEpoch(String cntrTm) {
        if (cntrTm == null || cntrTm.length() != 14) return 0L;
        int year  = Integer.parseInt(cntrTm.substring(0, 4));
        int month = Integer.parseInt(cntrTm.substring(4, 6));
        int day   = Integer.parseInt(cntrTm.substring(6, 8));
        int hour  = Integer.parseInt(cntrTm.substring(8, 10));
        int min   = Integer.parseInt(cntrTm.substring(10, 12));
        int sec   = Integer.parseInt(cntrTm.substring(12, 14));

        LocalDateTime dt = LocalDateTime.of(year, month, day, hour, min, sec);
        return dt.toEpochSecond(ZoneOffset.UTC); // 한국 시간
    }
}