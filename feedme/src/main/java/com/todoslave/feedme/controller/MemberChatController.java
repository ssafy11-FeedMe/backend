package com.todoslave.feedme.controller;

import com.todoslave.feedme.domain.entity.communication.MemberChatMessage;
import com.todoslave.feedme.domain.entity.communication.MemberChatRoom;
import com.todoslave.feedme.domain.entity.membership.Member;
import com.todoslave.feedme.service.MemberChatService;
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
  public ResponseEntity<MemberChatRoom> findChatRoom(@RequestParam("memberId") int memberId,
      @RequestParam("counterpartId") int counterpartId) {
    if (memberId != 0 || counterpartId != 0) {
      return ResponseEntity.badRequest().build();
    }

    MemberChatRoom room = new MemberChatRoom();

    Member member = new Member();
    member.setId(memberId);
    room.setMember(member);
    room.setCounterpartId(counterpartId);

    return ResponseEntity.ok(chatService.getChatRoom(room));

  }

  @GetMapping("friends/chats")
  public ResponseEntity<Slice<MemberChatMessage>> findMessages(@RequestParam int roomId,
      @RequestParam int page,
      @RequestParam int size){

    MemberChatRoom room = new MemberChatRoom();
    room.setId(roomId);

    return ResponseEntity.ok(chatService.getChatMessage(room, page, size));
  }

  @MessageMapping("/{roomId}/message")
  @SendTo("/{roomId}")
  public ResponseEntity<MemberChatMessage> sendMessage(@DestinationVariable int roomId,
      @Payload MemberChatMessage memberChatMessage){
    MemberChatMessage message = new MemberChatMessage();
    MemberChatRoom memberChatRoom = new MemberChatRoom();
    memberChatRoom.setId(roomId);
    message.setMemberChatRoom(memberChatRoom);
    return ResponseEntity.ok(chatService.insertChatMessage(message));
  }

}
