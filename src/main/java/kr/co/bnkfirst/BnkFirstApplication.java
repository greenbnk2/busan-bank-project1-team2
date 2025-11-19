package kr.co.bnkfirst;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//Security 의존성 임시 제거
@SpringBootApplication(
        exclude = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        }
)
@EnableScheduling
public class BnkFirstApplication {

    public static void main(String[] args) {
        SpringApplication.run(BnkFirstApplication.class, args);
    }

}
