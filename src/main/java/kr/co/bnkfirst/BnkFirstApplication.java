package kr.co.bnkfirst;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//Security 의존성 임시 제거
@SpringBootApplication(
        exclude = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        }
)
public class BnkFirstApplication {

    public static void main(String[] args) {
        SpringApplication.run(BnkFirstApplication.class, args);
    }

}
