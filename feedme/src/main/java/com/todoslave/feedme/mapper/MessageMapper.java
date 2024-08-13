package com.todoslave.feedme.mapper;

import com.todoslave.feedme.DTO.MemberChatMessageResponseDTO;
import com.todoslave.feedme.domain.entity.communication.MemberChatMessage;
import com.todoslave.feedme.domain.entity.membership.Member;
import com.todoslave.feedme.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor
public class MessageMapper {

  @Autowired
  static MemberRepository memberRepository;

  public static MemberChatMessageResponseDTO toDto(MemberChatMessage message){

    MemberChatMessageResponseDTO dto = new MemberChatMessageResponseDTO();

    Member member = memberRepository.getById(message.getSendId());
    dto.setSendNickname(member.getNickname());
    dto.setMessage(message.getContent());
    dto.setTransmitAt(message.getTransmitAt());

    return dto;
  }

}
