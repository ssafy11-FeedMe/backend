package com.todoslave.feedme.repository;


import com.todoslave.feedme.domain.entity.communication.MemberChatRoom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberChatRoomRepository extends CrudRepository<MemberChatRoom, Integer> {

  // 방 번호 얻어오기
  MemberChatRoom findByMemberIdAndCounterpartId(int memberId, int counterpartId);

  // 방 생성
  MemberChatRoom save(MemberChatRoom room);

}
