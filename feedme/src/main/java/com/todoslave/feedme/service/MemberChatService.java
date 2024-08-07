package com.todoslave.feedme.service;

import com.todoslave.feedme.DTO.MemberChatMessageRequestDTO;
import com.todoslave.feedme.DTO.MemberChatMessageResponseDTO;
import com.todoslave.feedme.domain.entity.communication.MemberChatMessage;
import com.todoslave.feedme.domain.entity.communication.MemberChatRoom;
import java.util.List;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
public interface MemberChatService {

  // 채팅방 목록 불러오기
  public List<MemberChatRoom> getChatRooms();

  // 채팅방 생성
  public MemberChatRoom insertChatRoom(List<Integer> members);

  // 채팅방 메세지 불러오기
  public Slice<MemberChatMessage> getChatMessage(String roomId, int page, int size);

  // 채팅방 메세지 저장
  public MemberChatMessageResponseDTO insertChatMessage(String roomId, MemberChatMessageRequestDTO memberChatMessageRequestDTO);

}