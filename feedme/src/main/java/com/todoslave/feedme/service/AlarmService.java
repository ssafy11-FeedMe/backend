package com.todoslave.feedme.service;

import com.todoslave.feedme.domain.entity.communication.MemberChatMessage;
import com.todoslave.feedme.domain.entity.membership.Member;
import java.time.LocalDateTime;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AlarmService {

    SseEmitter createEmitter();
    void todoCompleted();
    boolean requestFriendship(Member counterpart);
    void congratsBirthday();
    void renewChattingRoom(MemberChatMessage memberChatMessage);
    void checkAlarm(LocalDateTime checkTime);

}
