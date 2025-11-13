package kr.co.bnkfirst.mapper;

import kr.co.bnkfirst.dto.DocumentDTO;
import kr.co.bnkfirst.dto.MainEventDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

}
