package kr.co.bnkfirst.dto.info;

import lombok.Data;
import java.time.LocalDate;

@Data
public class NewsDTO extends BoardBaseDTO {
    private int newsId;        // 식별번호
    private String category;   // 카테고리
    private String filePath;   // 첨부파일 경로
    private LocalDate regDate; // 등록일
}
