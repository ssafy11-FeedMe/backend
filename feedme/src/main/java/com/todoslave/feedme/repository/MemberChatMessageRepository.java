package com.todoslave.feedme.repository;

import com.todoslave.feedme.domain.entity.communication.MemberChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberChatMessageRepository extends
    PagingAndSortingRepository<MemberChatMessage, Integer> {

  // 메세지 불러오기
  Slice<MemberChatMessage> findSliceBymemberChatRoomId(int memberChatRoomId, Pageable pageable);

  // 메세지 저장
  MemberChatMessage save(MemberChatMessage message);

}
