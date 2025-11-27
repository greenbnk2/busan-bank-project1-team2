package kr.co.bnkfirst.kiwoomETF;

import jakarta.annotation.PostConstruct;
import kr.co.bnkfirst.kiwoom.KiwoomTrClient;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Getter
public class EtfService {

    private final KiwoomTrClient trClient;

    private volatile List<EtfQuoteDTO> cachedEtfs = new ArrayList<>();

    @PostConstruct
    public void init() {
        refreshEtfQuotes();
    }

    // 1.5초마다 정도만 갱신해도 충분
    @Scheduled(fixedRate = 1500)
    public void refreshEtfQuotes() {
        try {
            List<EtfQuoteDTO> list = fetchEtfQuotes(100); // TOP 100 정도
            cachedEtfs = list;
        } catch (Exception e) {
            log.error("ETF 전체시세 갱신 실패", e);
        }
    }

    public List<EtfQuoteDTO> getCachedEtfs() {
        return cachedEtfs;
    }

    private List<EtfQuoteDTO> fetchEtfQuotes(int limit) {
        Map<String, String> input = new HashMap<>();
        // 문서 기준 기본값 (전체 조회)
        input.put("txon_type", "0");   // 과세유형 전체
        input.put("navpre", "0");      // NAV대비 전체
        input.put("mngmcomp", "0000"); // 운용사 전체
        input.put("txon_yn", "0");     // 과세여부 전체
        input.put("trace_idex", "0");  // 추적지수 전체
        input.put("stex_tp", "1");     // 1:KRX (필요에 따라 조정)

        List<Map<String, String>> rows =
                trClient.call("ka40004", "etfall_mrpr", input, limit);

        List<EtfQuoteDTO> list = new ArrayList<>();
        int rank = 1;
        for (Map<String, String> row : rows) {
            EtfQuoteDTO dto = toDto(row, rank);
            if (dto != null) {
                list.add(dto);
                rank++;
                if (list.size() >= limit) break;
            }
        }
        return list;
    }

    private EtfQuoteDTO toDto(Map<String, String> row, int rank) {
        try {
            String code = row.get("stk_cd");
            String name = row.get("stk_nm");

            long price = parseLong(row.get("close_pric"));
            double changeRate = parseDouble(row.get("pre_rt"));
            double nav = parseDouble(row.get("nav"));
            double premiumRate = 0.0;

            if (nav != 0.0) {
                premiumRate = (price - nav) / nav * 100.0;
            }

            String traceIndexName = row.get("trace_idex_nm");

            return EtfQuoteDTO.builder()
                    .rank(rank)
                    .code(code)
                    .name(name)
                    .price(price)
                    .changeRate(changeRate)
                    .nav(nav)
                    .premiumRate(premiumRate)
                    .traceIndexName(traceIndexName)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private long parseLong(String s) {
        if (s == null) return 0L;
        String cleaned = s.trim().replace(",", "");
        if (cleaned.isEmpty()) return 0L;
        return Long.parseLong(cleaned);
    }

    private double parseDouble(String s) {
        if (s == null) return 0.0;
        String cleaned = s.trim().replace(",", "");
        if (cleaned.isEmpty()) return 0.0;
        return Double.parseDouble(cleaned);
    }

    public List<EtfQuoteDTO> getEtfRanks() {
        // 🔥 기존 캐시 가져오기
        List<EtfQuoteDTO> list = new ArrayList<>(cachedEtfs);

        // 🔥 등락률 기준 내림차순 정렬
        list.sort((a, b) -> Double.compare(
                b.getChangeRate(),
                a.getChangeRate()
        ));

        // 🔥 rank 다시 부여
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setRank(i + 1);
        }

        return list;
    }

    public EtfQuoteDTO findByCode(String code) {
        if (code == null) return null;
        return cachedEtfs.stream()
                .filter(e -> code.equals(e.getCode()))
                .findFirst()
                .orElse(null);
    }
}
