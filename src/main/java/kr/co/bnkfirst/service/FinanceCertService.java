package kr.co.bnkfirst.service;

import kr.co.bnkfirst.dto.FinanceCertResult;
import org.springframework.stereotype.Service;

@Service
public class FinanceCertService {

    public String buildAuthUrl(String txId) {
        return "https://finance-cert.example.com/auth?"
                + "tx_id=" + txId
                + "&redirect_uri=http://localhost:8080/BNK/member/auth/finance-cert/callback";
    }

    public FinanceCertResult fetchResult(String txId, String code) {

        // 테스트용 더미
        FinanceCertResult r = new FinanceCertResult();
        r.setName("홍길동");
        r.setBirth("1995-07-03");
        r.setGender("M");
        r.setCarrier("SKT");
        r.setPhone("01012345678");
        r.setCi("FAKE-CI-1234567890");
        return r;
    }
}