package kr.co.bnkfirst.service;

import kr.co.bnkfirst.dto.DocumentDTO;
import kr.co.bnkfirst.mapper.DocumentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentMapper documentMapper;

    public List<DocumentDTO> getAllDocuments(String type) {
        return documentMapper.selectAllDocumentsByType(type);
    }

    public void insertDocument(DocumentDTO dto){
        documentMapper.insertDocument(dto);
    }

    public List<DocumentDTO> searchDocuments(String keyword) {
        return documentMapper.searchDocuments(keyword);
    }
}
