package kr.co.bnkfirst;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
// 개인 컴퓨터 환경에서 Kiwoom 실행에서 제외
//@ComponentScan(excludeFilters = {
//        @ComponentScan.Filter(
//                type = FilterType.REGEX,
//                pattern = {
//                        "kr\\.co\\.bnkfirst\\.kiwoom\\..*",
//                        "kr\\.co\\.bnkfirst\\.kiwoomRank\\..*",
//                        "kr\\.co\\.bnkfirst\\.config\\.WebSocketConfig"
//                }
//        ),
//        @ComponentScan.Filter(
//                type = FilterType.ASSIGNABLE_TYPE,
//                classes = {
//                        kr.co.bnkfirst.controller.StockController.class,
//                        kr.co.bnkfirst.controller.KiwoomTestController.class
//                }
//        )
//})
// DB증권 API 토큰 발행 이슈 해결될 때까지 프로젝트에서 제외
@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(
                type = FilterType.REGEX,
                pattern = {
                        "kr\\.co\\.bnkfirst\\.dbstock\\..*",
                        "kr\\.co\\.bnkfirst\\.dbstockrank\\..*",
//                        "kr\\.co\\.bnkfirst\\.config\\.WebSocketConfig"
                }
        ),
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {
//                        kr.co.bnkfirst.controller.StockController.class,
//                        kr.co.bnkfirst.controller.KiwoomTestController.class
                }
        )
})
@EnableScheduling
public class BnkFirstApplication {

    public static void main(String[] args) {
        SpringApplication.run(BnkFirstApplication.class, args);
    }

}
