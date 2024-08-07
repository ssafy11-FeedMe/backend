package com.todoslave.feedme.repository;

import com.todoslave.feedme.domain.entity.alarm.Alarm;
import com.todoslave.feedme.domain.entity.membership.MemberAlarm;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;


public interface MemberAlarmRepository extends JpaRepository<MemberAlarm, Integer> {

  boolean existsByMemberIdAndReceiveAt(@Param("memberId") int memberId, @Param("receiveAt") int receiveAt);

  List<Alarm> findByMemberIdAndReceiveAtAndIsChecked(@Param("memberId") int memberId, @Param("receiveAt") int receiveAt, int isChecked);

}
