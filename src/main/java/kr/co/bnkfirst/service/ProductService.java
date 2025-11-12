package kr.co.bnkfirst.service;

import kr.co.bnkfirst.dto.product.ProductDTO;
import kr.co.bnkfirst.entity.product.Product;
import kr.co.bnkfirst.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private static final List<String> ORDER_JOIN   = List.of("인터넷", "영업점", "스마트폰");
    private static final Set<String>  ALLOWED_JOIN = Set.copyOf(ORDER_JOIN);

    private static final List<String> ORDER_TARGET   = List.of("개인", "기업", "개인사업자");  // 예시
    private static final Set<String>  ALLOWED_TARGET = Set.copyOf(ORDER_TARGET);

    private static final List<String> ORDER_TAX   = List.of("비과세", "세금우대", "소득공제"); // 예시
    private static final Set<String>  ALLOWED_TAX = Set.copyOf(ORDER_TAX);

    public static String normalizeMulti(String raw, List<String> order, Set<String> allowed) {
        if (raw == null || raw.isBlank()) return null; // 필터 미적용
        return Arrays.stream(raw.split("\\s*,\\s*"))     // 쉼표 기준, 양옆 공백 무시
                .map(String::trim)
                .filter(s -> !s.isBlank() && allowed.contains(s)) // 허용값만
                .distinct()                                      // 중복 제거
                .sorted(Comparator.comparingInt(order::indexOf)) // 고정 순서
                .collect(Collectors.joining(","));
    }

    public Page<ProductDTO> findProducts(String sort,
                                         int page,
                                         int pageSize,
                                         String target,
                                         String join,
                                         String tax,
                                         String keyword) {
        Page<Product> products = null;
        Pageable pageable = null;
        String normTarget = normalizeMulti(target, ORDER_TARGET, ALLOWED_TARGET);
        String normJoin = normalizeMulti(join, ORDER_JOIN, ALLOWED_JOIN);
        String normTax = normalizeMulti(tax, ORDER_TAX, ALLOWED_TAX);
        switch (sort) {
            case "join_internet" -> {
                pageable = PageRequest.of(page - 1, pageSize);
                products = productRepository.findPrefSorted(keyword, "인터넷", normTarget, normJoin, normTax, pageable);
                log.info("products = {}", products);
                return products.map(Product::toDTO);
            }
            case "rate_desc" -> {
                pageable = PageRequest.of(page - 1, pageSize, Sort.by("phirate").descending());
                products = productRepository.findDynamicProducts(normTarget, normJoin, normTax, pageable);
                log.info("products = {}", products);
                return products.map(Product::toDTO);
            }
            case "release_desc" -> {
                pageable = PageRequest.of(page - 1, pageSize, Sort.by("pupdate").descending());
                products = productRepository.findDynamicProducts(normTarget, normJoin, normTax, pageable);
                log.info("products = {}", products);
                return products.map(Product::toDTO);
            }
            default -> {
                return null;
            }
        }
    }

    @Transactional(readOnly = true) // 조회 전용이라는 힌트를 DB/JPA/Spring에게 주는 어노테이션
    public Optional<ProductDTO> findProductByPid(String pid) {
        if (pid == null || pid.isBlank()) return Optional.empty();
        return productRepository.findByPid(pid).map(Product::toDTO);
    }
}