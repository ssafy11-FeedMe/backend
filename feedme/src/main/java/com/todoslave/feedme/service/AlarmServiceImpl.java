package com.todoslave.feedme.service;


import com.todoslave.feedme.domain.entity.alarm.Alarm;
import com.todoslave.feedme.domain.entity.communication.MemberChatMessage;
import com.todoslave.feedme.domain.entity.membership.Member;
import com.todoslave.feedme.event.AlarmCreatedEvent;
import com.todoslave.feedme.login.util.SecurityUserDto;
import com.todoslave.feedme.login.util.SecurityUtil;
import com.todoslave.feedme.repository.AlarmRepository;
import com.todoslave.feedme.repository.MemberAlarmRepository;
import com.todoslave.feedme.repository.MemberRepository;
import com.todoslave.feedme.repository.TodoRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService{

  private AlarmRepository alarmRepository;
  private MemberAlarmRepository memberAlarmRepository;
  private TodoRepository todoRepository;

  private MemberRepository memberRepository;

  private ApplicationEventPublisher eventPublisher;
  private final Map<Integer, SseEmitter> emitters = new ConcurrentHashMap<>();

  @Override
  public SseEmitter createEmitter() {

    int memberId = SecurityUtil.getCurrentUserId();

    SseEmitter emitter = new SseEmitter();
    emitters.put(memberId, emitter);

    emitter.onCompletion(() -> emitters.remove(memberId));
    emitter.onTimeout(() -> emitters.remove(memberId));
    emitter.onError((e) -> emitters.remove(memberId));

    return emitter;
  }

  //1시간 마다 확인, 일정 완료 했는지 여부
  @Scheduled(cron = "0 0 * * * ?")
  public void todoCompleted(){

    LocalDate currentDay = LocalDate.now();
    List<Integer> members = todoRepository.findMemberIdAllByCreatedAtAndIsCompleted(currentDay);

    for(int memberId : members){

      LocalTime currentTime = LocalTime.now();
      int time = currentTime.getHour();

      if(memberAlarmRepository.existsByMemberIdAndReceiveAt(memberId,time)) {

        Alarm alarm = new Alarm();

        Member member = new Member();
        member.setId(memberId);

        alarm.setMember(member);
        alarm.setContent("(크리쳐 이름)"+"이 기다리고 있어요!");
        alarmRepository.save(alarm);

        eventPublisher.publishEvent(new AlarmCreatedEvent(this, alarm,"Todo"));
      }
    }
  }

  //친구 요청 알림
  public boolean requestFriendship(Member counterpart){

    Alarm alarm = new Alarm();
    SecurityUserDto member = SecurityUtil.getCurrentUser();

    alarm.setMember(memberRepository.getById(member.getId()));
    alarm.setMember(counterpart);
    alarm.setContent(member.getNickname()+"님이 친구 요청을 보내셨습니다.");

    Alarm a = alarmRepository.save(alarm);

    if(a==null){
      return false;
    }

    eventPublisher.publishEvent(new AlarmCreatedEvent(this, alarm,"Friend"));

    return true;
  }

  //생일 알림
  @Scheduled(cron = "0 0 0 * * *")
  public void congratsBirthday(){

    LocalDate date = LocalDate.now();
    List<Member> birthdayPerson = memberRepository.findAllByBirthday(date);

    Alarm alarm = new Alarm();

    for(Member member : birthdayPerson){

      alarm.setMember(member);
      alarm.setContent(member.getNickname()+"님! 생일 축하합니다!");

      alarmRepository.save(alarm);

      eventPublisher.publishEvent(new AlarmCreatedEvent(this, alarm,"Birthday"));

    }

  }

  // 채팅방 갱신
  @Override
  public void renewChattingRoom(MemberChatMessage memberChatMessage) {

    Alarm alarm = new Alarm();
    eventPublisher.publishEvent(new AlarmCreatedEvent(this, memberChatMessage,"Chatting"));

  }

  @Override
  @Transactional
  public void checkAlarm(LocalDateTime receiveAt) {

    int memberId = SecurityUtil.getCurrentUserId();
    List<Alarm> alarms = alarmRepository.findByMemberIdAndReceiveAtAndIsChecked(memberId,receiveAt,0);

    for(Alarm alarm : alarms){
      alarm.setIsChecked(1);
      alarmRepository.save(alarm);
    }

  }

}
