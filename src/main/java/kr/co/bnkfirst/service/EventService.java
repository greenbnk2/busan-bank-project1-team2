package kr.co.bnkfirst.service.info;

import kr.co.bnkfirst.dto.info.EventDTO;
import kr.co.bnkfirst.dto.info.PageRequestDTO;
import kr.co.bnkfirst.dto.info.PageResponseDTO;


public interface EventService {

    /** ✅ 이벤트 목록 조회 */
    PageResponseDTO<EventDTO> getEventList(PageRequestDTO pageRequestDTO);

    /** ✅ 이벤트 단건 조회 */
    EventDTO getEvent(int eventId);
}
