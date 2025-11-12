package kr.co.bnkfirst.dto.admin;

import kr.co.bnkfirst.dto.PageRequestDTO;
import kr.co.bnkfirst.dto.product.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResponseAdminProductDTO {

    private List<ProductDTO> dtoList;

    private String boardType; // 게시판 종류
    private int pg;
    private int size;
    private int total;
    private int startNo;
    private int start, end;
    private boolean prev, next; // 이전, 다음 버튼

    private String searchType;
    private String keyword;

    @Builder
    public PageResponseAdminProductDTO(PageRequestDTO pageRequestDTO, List<ProductDTO> dtoList, int total) {

        this.boardType = pageRequestDTO.getBoardType();
        this.pg = pageRequestDTO.getPg();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        this.startNo = total - ((pg - 1) * size);
        this.end = (int)(Math.ceil(this.pg / 5.0)) * 5;
        this.start = this.end - 4;

        int last = (int)(Math.ceil(total / (double) size));
        this.end = Math.min(end, last);
        this.prev = this.pg > 1;
        this.next = this.pg < last;

        this.searchType = pageRequestDTO.getSearchType();
        this.keyword = pageRequestDTO.getKeyword();
    }

}
