package kr.co.bnkfirst.mapper;

import kr.co.bnkfirst.dto.BranchDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BranchMapper {

    //전체조회
    @Select("SELECT * FROM BRANCH ORDER BY BRID ASC")
    List<BranchDTO> findAllBranches();

    // ✅ 페이징 조회용
    @Select("""
        SELECT *
        FROM BRANCH
        ORDER BY BRID ASC
        OFFSET #{offset} ROWS
        FETCH NEXT #{size} ROWS ONLY
    """)
    List<BranchDTO> findBranchPage(
            @Param("offset") int offset,
            @Param("size")   int size
    );

    // ✅ 전체 개수(페이지 계산용, 나중에 쓸 수도 있음)
    @Select("SELECT COUNT(*) FROM BRANCH")
    int countBranches();

    //검색기능 ( all, 지역명, 영업점명, 지점코드, 주소)
    @Select("""
            SELECT * FROM BRANCH
            WHERE LOWER(BRNAME) LIKE '%' || LOWER(#{keyword}) || '%'
               OR LOWER(BRADDR) LIKE '%' || LOWER(#{keyword}) || '%'
               OR LOWER(BRTEL) LIKE '%' || LOWER(#{keyword}) || '%'
               OR LOWER(BRFAX) LIKE '%' || LOWER(#{keyword}) || '%'
               OR TO_CHAR(BRID) LIKE '%' || #{keyword} || '%'
            ORDER BY BRID ASC
    """)
    List<BranchDTO> searchBranches(@Param("keyword") String keyword);

    // 🔥 영업점 삭제
    @Delete("DELETE FROM BRANCH WHERE BRID = #{brid}")
    int deleteBranch(@Param("brid") int brid);

    // 🔎 단일 영업점 조회
    @Select("SELECT * FROM BRANCH WHERE BRID = #{brid}")
    BranchDTO findBranchById(@Param("brid") int brid);

    // ✏️ 영업점 수정
    @Update("""
        UPDATE BRANCH
        SET BRNAME = #{brname},
            BRADDR = #{braddr},
            BRTEL  = #{brtel},
            BRFAX  = #{brfax}
        WHERE BRID = #{brid}
    """)
    int updateBranch(BranchDTO dto);

    @Insert("""
    INSERT INTO BRANCH (BRID, BRNAME, BRADDR, BRTEL, BRFAX)
    VALUES (BRANCH_SEQ.NEXTVAL, #{brname}, #{braddr}, #{brtel}, #{brfax})
""")
    int insertBranch(BranchDTO dto);


}
