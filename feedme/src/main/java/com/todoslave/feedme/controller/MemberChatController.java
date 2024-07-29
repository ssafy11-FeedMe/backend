package com.todoslave.feedme.controller;

import com.todoslave.feedme.domain.entity.communication.MemberChatMessage;
import com.todoslave.feedme.domain.entity.communication.MemberChatRoom;
import com.todoslave.feedme.service.MemberChatService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  // 이 유저가 누구랑 채팅방을 갖고 있는 지 찾기
  @GetMapping("/{memberId}")
  public ResponseEntity<List<MemberChatRoom>> findChatRoomList(@PathVariable("memberId") String memberId){

    MemberChatRoom room = new MemberChatRoom();
    List<String> members = new ArrayList<>();
    members.add(memberId);
    room.setParticipantIds(members);

    return ResponseEntity.ok(chatService.getChatRooms(room));
  }

  // 채팅방 ID 찾기
  @PostMapping
  public ResponseEntity<MemberChatRoom> findChatRoom(@RequestParam("memberId") String memberId,
                                                     @RequestParam("counterpartId") String counterpartId) {

    if (memberId == null || counterpartId == null) {
      return ResponseEntity.badRequest().build();
    }

    MemberChatRoom room = new MemberChatRoom();

    List<String> members = new ArrayList<>();

    members.add(memberId);
    members.add(counterpartId);

    room.setParticipantIds(members);

    return ResponseEntity.ok(chatService.getChatRoom(room));

  }

  // 메세지 불러오기
  @GetMapping
  public ResponseEntity<Slice<MemberChatMessage>> findMessages(@RequestParam String roomId,
                                                               @RequestParam int page,
                                                               @RequestParam int size){

    MemberChatRoom room = new MemberChatRoom();
    room.setId(roomId);

    return ResponseEntity.ok(chatService.getChatMessage(room, page, size));
  }

  // 메세지 저장
  @MessageMapping("/messages")
  @SendTo("/topic/messages")
  public MemberChatMessage sendMessage(@Payload MemberChatMessage memberChatMessage) {
    return chatService.insertChatMessage(memberChatMessage);
  }
}
