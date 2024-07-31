package com.todoslave.feedme.repository;

import com.todoslave.feedme.domain.entity.membership.MemberAlarm;
import java.sql.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MemberAlarmRepository extends JpaRepository<MemberAlarm, Integer> {

  @Query("SELECT m.id FROM Member m JOIN m.alarms a WHERE FUNCTION('HOUR', a.alarmTime) = FUNCTION('HOUR', :alarmTime)")
  int findMemberIdByAlarmTimeHour(@Param("alarmTime") int alarmTime);

}
