package com.todoslave.feedme.mapper;

import com.todoslave.feedme.DTO.FriendReqResponseDTO;
import com.todoslave.feedme.domain.entity.communication.FriendRequest;

public class FriendRequestMapper {

  public static FriendReqResponseDTO toDto(FriendRequest friendRequest){
    FriendReqResponseDTO dto = new FriendReqResponseDTO();
    dto.setId(friendRequest.getId());
    dto.setCounterpartNickname(friendRequest.getCounterpartId().getNickname());
    return dto;
  }

}
