package com.todoslave.feedme.service;

import com.todoslave.feedme.DTO.MemberChatMessageRequestDTO;
import com.todoslave.feedme.DTO.MemberChatMessageResponseDTO;
import com.todoslave.feedme.domain.entity.communication.MemberChatMessage;
import com.todoslave.feedme.domain.entity.communication.MemberChatRoom;
import com.todoslave.feedme.domain.entity.communication.MemberChatRoomChecked;
import com.todoslave.feedme.domain.entity.membership.Member;
import com.todoslave.feedme.login.util.SecurityUtil;
import com.todoslave.feedme.repository.MemberChatMessageRepository;
import com.todoslave.feedme.repository.MemberChatRoomCheckedRepository;
import com.todoslave.feedme.repository.MemberChatRoomRepository;
import com.todoslave.feedme.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberChatServiceImpl implements MemberChatService{

  private final MemberChatMessageRepository messageRepository;
  private final MemberChatRoomRepository roomRepository;
  private final MemberChatRoomCheckedRepository roomCheckedRepository;
  private final AlarmService alarmService;

  @Override
  public List<MemberChatRoom> getChatRooms() {

    int memberId = SecurityUtil.getCurrentUserId();

    MemberChatRoom memberChatRoom = new MemberChatRoom();
    List<Integer> members = new ArrayList<>();
    members.add(memberId);

    List<MemberChatRoom> rooms = roomRepository.findAllByParticipantIdsContaining(members);


  }

  public MemberChatRoom insertChatRoom(List<Integer> members){

    MemberChatRoom room = new MemberChatRoom();
    room.setParticipantIds(members);
    room = roomRepository.save(room);

    MemberChatRoomChecked checked = new MemberChatRoomChecked();
    checked.setMemberId(members.get(0));
    checked.setIsChecked(1);
    checked.setMemberChatRoomId(room.getId());
    roomCheckedRepository.save(checked);

    checked.setMemberId(members.get(1));
    roomCheckedRepository.save(checked);


  }

  public Slice<MemberChatMessage> getChatMessage(String roomId, int skip, int limit){

    System.out.println("receive message? service");
    Pageable pageable = PageRequest.of(skip / limit, limit);

    Slice<MemberChatMessage> messages = messageRepository.findByMemberChatRoomIdOrderByTransmitAtDesc(roomId, pageable);

    return messages;
  }

  public MemberChatMessageResponseDTO insertChatMessage(String roomId, MemberChatMessageRequestDTO memberChatMessageRequestDTO) {
    MemberChatMessage memberChatMessage = new MemberChatMessage();

    int memberId = SecurityUtil.getCurrentUserId();

    memberChatMessage.setMemberChatRoomId(roomId);
    memberChatMessage.setContent(memberChatMessageRequestDTO.getMessage());
    memberChatMessage.setSendId(memberId);

    memberChatMessage = messageRepository.save(memberChatMessage);



    alarmService.renewChattingRoom(memberChatMessage);

    MemberChatMessageResponseDTO response = new MemberChatMessageResponseDTO();

    response.setMessage(memberChatMessage.getContent());
    response.setTransmitAt(memberChatMessage.getTransmitAt());
    response.setSendNickname(SecurityUtil.getCurrentMember().getNickname());

    return response;
  }

}
