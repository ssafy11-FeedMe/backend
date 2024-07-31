package com.todoslave.feedme.service;


import com.todoslave.feedme.domain.entity.membership.MemberAlarm;
import com.todoslave.feedme.domain.entity.task.Todo;
import com.todoslave.feedme.repository.AlarmRepository;
import com.todoslave.feedme.repository.MemberAlarmRepository;
import com.todoslave.feedme.repository.TodoRepository;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService{

  private AlarmRepository alarmRepository;
  private MemberAlarmRepository memberAlarmRepository;
  private TodoRepository todoRepository;

  //1시간 마다 확인
  @Scheduled(fixedDelay = 3600000)
  public void checkUserTodo(){

    LocalDate currentDay = LocalDate.now();
    List<Integer> members = todoRepository.findMemberIdAllByCreatedAtAndIsCompletedFalse(currentDay);

    for(int memberId : members){

      LocalTime currentTime = LocalTime.now();
      int time = currentTime.getHour();
      int id = memberAlarmRepository.findMemberIdByAlarmTimeHour(time);

      MemberAlarm alarm = new MemberAlarm();

//      Member member =
//      alarm.setMember()
//
//      alarmRepository.save();

    }

  }

}
