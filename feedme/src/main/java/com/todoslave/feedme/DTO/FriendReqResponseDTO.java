package com.todoslave.feedme.DTO;

import lombok.Data;

@Data
public class FriendReqResponseDTO {

  // 친구 요청 ID
  private int id;

  // 상대방 닉네임
  private String counterpartNickname;

  // 상대방 크리쳐 이미지
  private String creatureImg;

}
