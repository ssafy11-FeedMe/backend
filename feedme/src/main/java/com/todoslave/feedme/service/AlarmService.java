package com.todoslave.feedme.service;

import com.todoslave.feedme.DTO.AlarmResponseDTO;
import com.todoslave.feedme.DTO.FriendReqResponseDTO;
import com.todoslave.feedme.DTO.MemberChatListResponseDTO;
import com.todoslave.feedme.DTO.PaginationRequestDTO;
import com.todoslave.feedme.domain.entity.alarm.Alarm;
import com.todoslave.feedme.domain.entity.communication.MemberChatMessage;
import com.todoslave.feedme.domain.entity.membership.Member;
import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.data.domain.Slice;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AlarmService {

    SseEmitter createEmitter();
    void todoCompleted() throws IOException;
    void requestFriendship(FriendReqResponseDTO friendReqResponseDTO) throws IOException;
    void congratsBirthday() throws IOException;
    SseEmitter renewCreateEmitter();
    SseEmitter friendCreateEmitter();
    void renewChattingRoom(MemberChatListResponseDTO room) throws IOException;
    void sendAlarm(Alarm alarm) throws IOException;
    Slice<AlarmResponseDTO> loadAlarms(PaginationRequestDTO paginationRequestDTO);


}
