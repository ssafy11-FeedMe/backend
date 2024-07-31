package com.todoslave.feedme.repository;

import com.todoslave.feedme.domain.entity.membership.MemberAlarm;
import java.sql.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MemberAlarmRepository extends JpaRepository<MemberAlarm, Integer> {

  @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Member m JOIN m.alarms a WHERE m.id = :memberId AND FUNCTION('HOUR', a.alarmTime) = FUNCTION('HOUR', :alarmTime)")
  boolean existsByMemberIdAndAlarmTimeHour(@Param("memberId") int memberId, @Param("alarmTime") int alarmTime);

}
