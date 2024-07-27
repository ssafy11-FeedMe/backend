package com.todoslave.feedme.controller;

import com.todoslave.feedme.domain.entity.communication.MemberChatMessage;
import com.todoslave.feedme.domain.entity.communication.MemberChatRoom;
import com.todoslave.feedme.domain.entity.membership.Member;
import com.todoslave.feedme.service.MemberChatService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/friends/chats")
public class MemberChatController {

  @Autowired
  private final MemberChatService chatService;

  @PostMapping("/friends/chats")
  public ResponseEntity<MemberChatRoom> findChatRoom(@RequestParam("memberId") String memberId,
      @RequestParam("counterpartId") String counterpartId) {
    if (memberId != null || counterpartId != null) {
      return ResponseEntity.badRequest().build();
    }

    MemberChatRoom room = new MemberChatRoom();

<<<<<<< HEAD
    List<String> members = new ArrayList<>();

    members.add(memberId);
    members.add(counterpartId);

    room.setParticipantIds(members);
=======
    Member member = new Member();
    member.setId(memberId);
    room.setMember(member);
    room.setCounterpartId(counterpartId);
>>>>>>> df102da768be1bb2c749385d38cdad44cb0bfbc0

    return ResponseEntity.ok(chatService.getChatRoom(room));

  }

  @GetMapping("friends/chats")
  public ResponseEntity<Slice<MemberChatMessage>> findMessages(@RequestParam String roomId,
      @RequestParam int page,
      @RequestParam int size){

    MemberChatRoom room = new MemberChatRoom();
    room.setId(roomId);

    return ResponseEntity.ok(chatService.getChatMessage(room, page, size));
  }

  @MessageMapping("/{roomId}/message")
  @SendTo("/{roomId}")
  public ResponseEntity<MemberChatMessage> sendMessage(@DestinationVariable String roomId,

      @Payload MemberChatMessage memberChatMessage){
    MemberChatMessage message = new MemberChatMessage();
    message.setMemberChatRoomId(roomId);
    return ResponseEntity.ok(chatService.insertChatMessage(message));
  }

}
