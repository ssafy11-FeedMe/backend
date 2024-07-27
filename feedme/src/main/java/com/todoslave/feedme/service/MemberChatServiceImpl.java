package com.todoslave.feedme.service;

import com.todoslave.feedme.domain.entity.communication.MemberChatMessage;
import com.todoslave.feedme.domain.entity.communication.MemberChatRoom;
import com.todoslave.feedme.repository.MemberChatMessageRepository;
import com.todoslave.feedme.repository.MemberChatRoomRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberChatServiceImpl implements MemberChatService{

  @Autowired
  private MemberChatMessageRepository messageRepository;

  @Autowired
  private MemberChatRoomRepository roomRepository;

  public MemberChatRoom getChatRoom(MemberChatRoom room){

    room = roomRepository.findByParticipantIdsContaining(room.getParticipantIds());

    if(room==null){
      room = roomRepository.save(room);
    }

    return room;
  }

  public Slice<MemberChatMessage> getChatMessage(MemberChatRoom room, int page, int size){

    Pageable pageable = PageRequest.of(page, size);
    return messageRepository.findSliceByMemberChatRoomId(room.getId(), pageable);

  }

  public MemberChatMessage insertChatMessage(MemberChatMessage message) {
    return messageRepository.save(message);
  }

}
