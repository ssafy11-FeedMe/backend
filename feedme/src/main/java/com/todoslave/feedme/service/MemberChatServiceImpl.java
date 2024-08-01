package com.todoslave.feedme.service;

import com.todoslave.feedme.DTO.ChatFriendCreateDTO;
import com.todoslave.feedme.DTO.ChatFriendFindDTO;
import com.todoslave.feedme.domain.entity.communication.MemberChatMessage;
import com.todoslave.feedme.domain.entity.communication.MemberChatRoom;
import com.todoslave.feedme.domain.entity.membership.Member;
import com.todoslave.feedme.repository.MemberChatMessageRepository;
import com.todoslave.feedme.repository.MemberChatRoomRepository;
import com.todoslave.feedme.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberChatServiceImpl implements MemberChatService{

  private MemberChatMessageRepository messageRepository;
  private MemberChatRoomRepository roomRepository;
  private MemberRepository memberRepository;
  
  

  @Override
  public List<MemberChatRoom> getChatRooms(ChatFriendFindDTO chatFriendFindDTO) {
    List<MemberChatRoom> rooms = roomRepository.findAllByParticipantIdsContaining(chatFriendFindDTO.getMemberId());
    
    int counterpartId = -1;
    
    for(MemberChatRoom room : rooms){
      
      List<Integer> members = room.getParticipantIds();

      for(Integer member : members){
        if(chatFriendFindDTO.getMemberId().get(0)==member){
          continue;
        }
        counterpartId = member;
      }
      
//      Member member = memberRepository.findById(counterpartId);



    }
    
  }

  public MemberChatRoom getChatRoom(ChatFriendCreateDTO chatFriendCreateDTO){

    result = roomRepository.save(chatFriendCreateDTO.getMembers());

    return result;
  }

  public Slice<MemberChatMessage> getChatMessage(String roomId, int skip, int limit){

    System.out.println("receive message? service");
    Pageable pageable = PageRequest.of(skip / limit, limit);

    Slice<MemberChatMessage> messages = messageRepository.findByMemberChatRoomIdOrderByTransmitAtDesc(roomId, pageable);

    return messages;
  }

  public MemberChatMessage insertChatMessage(MemberChatMessage message) {
    System.out.println(message);
    return messageRepository.save(message);
  }

}
