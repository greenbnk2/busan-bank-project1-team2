package kr.co.bnkfirst;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@MapperScan("kr.co.bnkfirst.mapper")
class BnkFirstApplicationTests {
    @Test
    void contextLoads() {
    }

}
