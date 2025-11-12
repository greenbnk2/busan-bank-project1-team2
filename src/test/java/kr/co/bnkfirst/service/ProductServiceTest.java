package kr.co.bnkfirst.service;

import kr.co.bnkfirst.dto.product.ProductDTO;
import kr.co.bnkfirst.mapper.BranchMapper;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
<<<<<<< HEAD
import org.springframework.test.context.ActiveProfiles;
=======
>>>>>>> d6d9909d342a3630477264745a927fb931c7bce1

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @MockitoBean
    kr.co.bnkfirst.mapper.BranchMapper branchMapper;

    @Autowired
    ProductService productService;

    @MockitoBean
    BranchMapper branchMapper;

    @Test
    void findProductByPid() {
        Optional<ProductDTO> dto = productService.findProductByPid("BNK-TD-1");
        System.out.println(dto.isPresent());
        assertNotNull(dto.get());
        assertEquals("BNK-TD-1", dto.get().getPid());
        System.out.println(dto.get());
    }
}