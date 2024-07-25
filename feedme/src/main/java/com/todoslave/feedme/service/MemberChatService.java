package com.todoslave.feedme.service;

import com.todoslave.feedme.domain.entity.communication.MemberChatMessage;
import com.todoslave.feedme.domain.entity.communication.MemberChatRoom;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
public interface MemberChatService {

  // 채팅방 생성 or 불러오기
  public MemberChatRoom getChatRoom(MemberChatRoom room);

  // 채팅방 메세지 불러오기
  public Slice<MemberChatMessage> getChatMessage(MemberChatRoom room, int page, int size);

  // 채팅방 메세지 저장
  public MemberChatMessage insertChatMessage(MemberChatMessage message);

}