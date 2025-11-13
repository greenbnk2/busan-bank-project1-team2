package kr.co.bnkfirst.service;

import kr.co.bnkfirst.dto.product.ProductDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @MockitoBean
    kr.co.bnkfirst.mapper.BranchMapper branchMapper;

    @Autowired
    ProductService productService;

    @Test
    void findProductByPid() {
        Optional<ProductDTO> dto = productService.findProductByPid("BNK-TD-1");
        System.out.println(dto.isPresent());
        assertNotNull(dto.get());
        assertEquals("BNK-TD-1", dto.get().getPid());
        System.out.println(dto.get());
    }
}