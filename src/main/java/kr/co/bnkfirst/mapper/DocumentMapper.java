package kr.co.bnkfirst.mapper;

import kr.co.bnkfirst.dto.DocumentDTO;
import kr.co.bnkfirst.dto.MainEventDTO;
import kr.co.bnkfirst.dto.PageRequestDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DocumentMapper {

    //전체조회
    @Select("SELECT * FROM DOCUMENT WHERE DOCTYPE = #{type} ORDER BY DOCID ASC")
    List<DocumentDTO> selectAllDocumentsByType(String type);

    //삽입
    @Insert("""
    INSERT INTO DOCUMENT (DOCTYPE, DOCTITLE, DOCCONTENT)
    VALUES (#{doctype}, #{doctitle}, #{doccontent})
    """)
    void insertDocument(DocumentDTO dto);

    //검색기능 (제목 + 내용)
    @Select("""
    SELECT * FROM DOCUMENT
    WHERE DOCTYPE = #{doctype}
    AND (LOWER(DOCTITLE) LIKE '%' || LOWER(#{keyword}) || '%'
        OR LOWER(DOCCONTECT) LIKE '%' || LOWER(#{keyword}) || '%')
        ORDER BY DOCID ASC
    """)
    List<DocumentDTO> searchDocuments(String keyword);

    //최신 등록된 문서 4개
    @Select("""
    select
        *
    from DOCUMENT
    order by DOCUPDATE desc
    fetch first 4 rows only
    """)
    List<DocumentDTO> selectLatestDocuments4();

    // 메인페이지용 이벤트 조회 메서드 추가
    @Select("""
        SELECT EVID,
               EVTITLE,
               EVCONTENT,
               EVWRITER,
               TO_CHAR(EVREGDATE, 'YYYY-MM-DD') AS EVREGDATE
        FROM EVENT
        ORDER BY EVID DESC FETCH FIRST 4 ROWS ONLY
    """)
    List<MainEventDTO> selectMainEvents();


    // ===== 관리자(admin_cs) =====
    @Select("""
    SELECT
    DOCID, 
    MID, 
    DOCGROUP, 
    DOCTYPE, 
    DOCTITLE, 
    DOCANSWER,
    DOCFILE, 
    DOCUPDATE, 
    DOCCONTENT
    FROM DOCUMENT
    WHERE DOCTYPE = #{doctype}
    ORDER BY DOCID DESC
    """)
    List<DocumentDTO> selectAdminDocumentsAll(
            @Param("doctype") String doctype
    );


    @Select("""
    SELECT
        d.DOCID,
        d.MID,
        d.DOCGROUP,
        d.DOCTYPE,
        d.DOCTITLE,
        d.DOCANSWER,
        d.DOCFILE,
        d.DOCUPDATE,
        d.DOCCONTENT
    FROM DOCUMENT d
    WHERE d.DOCTYPE = #{doctype}       
    ORDER BY d.DOCID DESC
    OFFSET #{page.offset} ROWS
    FETCH NEXT #{page.size} ROWS ONLY
    """)
    List<DocumentDTO> selectAdminDocuments(
            @Param("doctype") String doctype,
            @Param("page") PageRequestDTO pageRequestDTO
    );

    @Select("""
    SELECT 
        COUNT(*) 
    FROM DOCUMENT d
    WHERE 1=1
      AND (#{doctype} IS NULL OR d.DOCTYPE = #{doctype})
""")
    int countAdminDocuments(
            @Param("doctype") String doctype,
            @Param("page") PageRequestDTO pageRequestDTO
    );


    // 단건 조회
    @Select("""
        SELECT
            DOCID,
            MID,
            DOCGROUP,
            DOCTYPE,
            DOCTITLE,
            DOCANSWER,
            DOCFILE,
            DOCUPDATE,
            DOCCONTENT
        FROM DOCUMENT
        WHERE DOCID = #{docid}
    """)
    DocumentDTO selectAdminDocumentById(@Param("docid") int docid);

    // 나머지 CRUD 는 일단 이렇게만 잡아두고 나중에 채워도 됨
    int insertAdminDocument(DocumentDTO dto);
    int updateAdminDocument(DocumentDTO dto);

    @Delete("DELETE FROM DOCUMENT WHERE DOCID = #{docid}")
    int deleteAdminDocument(@Param("docid") int docid);

}
