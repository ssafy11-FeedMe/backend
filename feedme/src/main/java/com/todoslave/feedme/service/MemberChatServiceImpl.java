package com.todoslave.feedme.service;

import com.todoslave.feedme.DTO.MemberChatListResponseDTO;
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
  private final MemberRepository memberRepository;
  private final MemberChatRoomCheckedRepository memberChatRoomCheckedRepository;

  // 채팅방 목록들 가져오기
  @Override
  public List<MemberChatListResponseDTO> getChatRooms() {

    int memberId = SecurityUtil.getCurrentUserId();

    MemberChatRoom memberChatRoom = new MemberChatRoom();
    List<Integer> members = new ArrayList<>();
    members.add(memberId);

    List<MemberChatRoom> rooms = roomRepository.findAllByParticipantIdsContainingOrderByReceiveTime(members);
    List<MemberChatListResponseDTO> chatListResponse = new ArrayList<>();

    for(MemberChatRoom room : rooms){

      MemberChatListResponseDTO chatResponse = new MemberChatListResponseDTO();
      chatResponse.setId(room.getId());

      members = room.getParticipantIds();
      int counterPartId = 0;

      for(int m : members){
        if(m!=memberId){
          counterPartId=m;
        }
      }

      String nickname = memberRepository.findById(counterPartId).orElseThrow().getNickname();
      chatResponse.setNickname(nickname);
//      chatResponse.setCreatureImage(
//              "http://localhost:8080/image/creature/"+{creature_id}+"_"+{creature_level});
      MemberChatRoomChecked checked = memberChatRoomCheckedRepository.findByMemberChatRoomIdAndMemberId(room.getId(),memberId);
      chatResponse.setIsChecked(checked.getIsChecked());

    }

    return chatListResponse;
  }

  // 채팅방 생성
  public MemberChatListResponseDTO insertChatRoom(List<Integer> members){

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

    Member countpart = null;

    for(int m : members){
      if(m!=SecurityUtil.getCurrentUserId()){
        countpart = memberRepository.findById(m).orElseThrow();
      }
    }

    MemberChatListResponseDTO memberChatListResponseDTO = new MemberChatListResponseDTO();
    memberChatListResponseDTO.setId(room.getId());
    memberChatListResponseDTO.setNickname(countpart.getNickname());
//    memberChatListResponseDTO.setCreatureImage(
//              "http://localhost:8080/image/creature/"+{creature_id}+"_"+{creature_level});
    memberChatListResponseDTO.setIsChecked(1);

    return memberChatListResponseDTO;
  }

  // 채팅방 메세지 불러오기
  public Slice<MemberChatMessage> getChatMessage(String roomId, int skip, int limit){

    System.out.println("receive message? service");
    Pageable pageable = PageRequest.of(skip / limit, limit);

    Slice<MemberChatMessage> messages = messageRepository.findByMemberChatRoomIdOrderByTransmitAtDesc(roomId, pageable);

    return messages;
  }

  // 채팅방 메세지 저장
  public MemberChatMessageResponseDTO insertChatMessage(String roomId, MemberChatMessageRequestDTO memberChatMessageRequestDTO) {
    MemberChatMessage memberChatMessage = new MemberChatMessage();

    int memberId = SecurityUtil.getCurrentUserId();

    MemberChatRoom memberChatRoom = roomRepository.findById(roomId).orElseThrow();
    String counterpartNickname = null;

    for(int m : memberChatRoom.getParticipantIds()){
      if(m!=memberId){
        counterpartNickname = memberRepository.findById(m).orElseThrow().getNickname();
      }
    }

    memberChatMessage.setMemberChatRoomId(roomId);
    memberChatMessage.setContent(memberChatMessageRequestDTO.getMessage());
    memberChatMessage.setSendId(memberId);

    memberChatMessage = messageRepository.save(memberChatMessage);

    MemberChatListResponseDTO memberChatListResponseDTO = new MemberChatListResponseDTO();
    memberChatListResponseDTO.setId(roomId);
    memberChatListResponseDTO.setNickname(counterpartNickname);
    memberChatListResponseDTO.setCreatureImage();

    alarmService.renewChattingRoom(memberChatMessage);

    MemberChatMessageResponseDTO response = new MemberChatMessageResponseDTO();

    response.setMessage(memberChatMessage.getContent());
    response.setTransmitAt(memberChatMessage.getTransmitAt());
    response.setSendNickname(SecurityUtil.getCurrentMember().getNickname());

    return response;
  }

}
