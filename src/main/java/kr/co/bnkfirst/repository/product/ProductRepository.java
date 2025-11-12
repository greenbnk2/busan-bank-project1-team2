package kr.co.bnkfirst.repository.product;

import kr.co.bnkfirst.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Integer> {
    @Query("""
    select p
    from Product p
    where (:pelgbl is null or p.pelgbl = :pelgbl)
      and (:prmthd is null or p.prmthd = :prmthd)
      and (:pprfcrt is null or p.pprfcrt = :pprfcrt)
    """)
    Page<Product> findDynamicProducts(
            @Param("pelgbl") String pelgbl,
            @Param("prmthd") String prmthd,
            @Param("pprfcrt") String pprfcrt,
            Pageable pageable
    );

    @Query("""
        select p
        from Product p
        where (:kw is null 
               or lower(p.pname) like lower(concat('%', :kw, '%')))
               and (:pelgbl is null or p.pelgbl = :pelgbl)
              and (:prmthd is null or p.prmthd = :prmthd)
              and (:pprfcrt is null or p.pprfcrt = :pprfcrt)
        order by 
          case 
            when function('instr', lower(p.prmthd), lower(:prefer)) > 0 then 0
            else 1 
          end,
          p.phirate desc,
          p.pname asc
    """)
    Page<Product> findPrefSorted(
            @Param("kw") String kw,               // (선택) 일반 검색어
            @Param("prefer") String prefer,       // 우선 배치하고 싶은 값 (예: "인터넷")
            @Param("pelgbl") String pelgbl,
            @Param("prmthd") String prmthd,
            @Param("pprfcrt") String pprfcrt,
            Pageable pageable                     // 페이지/사이즈만 사용 (Sort는 무시됨)
    );

    Optional<Product> findByPid(String pid);
}
