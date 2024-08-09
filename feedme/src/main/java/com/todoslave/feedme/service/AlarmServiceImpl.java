package com.todoslave.feedme.service;


import com.todoslave.feedme.DTO.AlarmResponseDTO;
import com.todoslave.feedme.DTO.FriendReqResponseDTO;
import com.todoslave.feedme.DTO.MemberChatListResponseDTO;
import com.todoslave.feedme.DTO.PaginationRequestDTO;
import com.todoslave.feedme.domain.entity.alarm.Alarm;
import com.todoslave.feedme.domain.entity.avatar.Creature;
import com.todoslave.feedme.domain.entity.communication.MemberChatMessage;
import com.todoslave.feedme.domain.entity.membership.Member;
import com.todoslave.feedme.login.util.SecurityUserDto;
import com.todoslave.feedme.login.util.SecurityUtil;
import com.todoslave.feedme.mapper.AlarmMapper;
import com.todoslave.feedme.repository.AlarmRepository;
import com.todoslave.feedme.repository.CreatureRepository;
import com.todoslave.feedme.repository.MemberAlarmRepository;
import com.todoslave.feedme.repository.MemberRepository;
import com.todoslave.feedme.repository.TodoRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@Service
public class AlarmServiceImpl implements AlarmService{

  private AlarmRepository alarmRepository;
  private MemberAlarmRepository memberAlarmRepository;
  private TodoRepository todoRepository;
  private MemberRepository memberRepository;
  private CreatureRepository creatureRepository;

  private final Map<Integer, SseEmitter> emitters = new ConcurrentHashMap<>();
  private final Map<Integer, SseEmitter> chatEmitters = new ConcurrentHashMap<>();
  private final Map<Integer, SseEmitter> friendEmitters = new ConcurrentHashMap<>();

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
  public void todoCompleted() throws IOException {

    LocalDate currentDay = LocalDate.now();
    List<Integer> members = todoRepository.findMemberIdAllByCreatedAtAndIsCompleted(currentDay);

    for(int memberId : members) {

      LocalTime currentTime = LocalTime.now();
      int time = currentTime.getHour();

      Alarm alarm = new Alarm();

      Member member = new Member();
      member.setId(memberId);

      Creature creature = creatureRepository.findByMemberId(memberId);

      alarm.setMember(member);
      alarm.setContent("배고픈 " + creature.getCreatureName() + ".. 밥 줄 사람 없나요?");
      alarmRepository.save(alarm);

      sendAlarm(alarm);

    }
  }

  //친구 요청 알림
  public void requestFriendship(FriendReqResponseDTO friendReqResponseDTO) throws IOException {

    int memberId = SecurityUtil.getCurrentUserId();
    SseEmitter emitter = friendEmitters.get(memberId);

    if(emitter!=null) {
      SseEmitter.SseEventBuilder event = SseEmitter.event()
          .name("friend")
          .data(friendReqResponseDTO);
      emitter.send(event);
    }else{
      System.out.println("Sse Connection is over" + memberId);
    }

  }

  //생일 알림
  @Scheduled(cron = "0 0 0 * * *")
  public void congratsBirthday() throws IOException {

    LocalDate date = LocalDate.now();
    List<Member> birthdayPerson = memberRepository.findAllByBirthday(date);

    Alarm alarm = new Alarm();

    for(Member member : birthdayPerson){

      alarm.setMember(member);
      alarm.setContent(member.getNickname()+"님! 생일 축하합니다!");

      alarmRepository.save(alarm);
      sendAlarm(alarm);

    }

  }

  // 채팅방 목록 페이지 들어왔을 때
  @Override
  public SseEmitter renewCreateEmitter() {
    int memberId = SecurityUtil.getCurrentUserId();

    SseEmitter emitter = new SseEmitter();
    chatEmitters.put(memberId, emitter);

    emitter.onCompletion(() -> chatEmitters.remove(memberId));
    emitter.onTimeout(() -> chatEmitters.remove(memberId));
    emitter.onError((e) -> emitters.remove(memberId));

    return emitter;
  }

  // 친구 창에서 SSE 구독
  @Override
  public SseEmitter friendCreateEmitter() {
    int memberId = SecurityUtil.getCurrentUserId();

    SseEmitter emitter = new SseEmitter();
    friendEmitters.put(memberId, emitter);

    emitter.onCompletion(() -> friendEmitters.remove(memberId));
    emitter.onTimeout(() -> friendEmitters.remove(memberId));
    emitter.onError((e) -> emitters.remove(memberId));

    return emitter;
  }

  // 채팅방 갱신
  @Override
  public void renewChattingRoom(MemberChatListResponseDTO room) throws IOException {

    int memberId = SecurityUtil.getCurrentUserId();

    SseEmitter emitter = chatEmitters.get(memberId);

    if(emitter!=null) {
      SseEmitter.SseEventBuilder event = SseEmitter.event()
          .name("chattingRoom")
          .data(room);

      emitter.send(event);
    }else{
      System.out.println("Friend don't connected" + memberId);
    }

  }

  // 알람 받아라
  @Override
  public void sendAlarm(Alarm alarm) throws IOException {

    int memberId = SecurityUtil.getCurrentUserId();
    SseEmitter emitter = emitters.get(memberId);

    if(emitter!=null){

      AlarmResponseDTO dto = AlarmMapper.toDto(alarm);

      SseEmitter.SseEventBuilder event = SseEmitter.event()
          .name("")
          .data(dto);

      emitter.send(event);
    }

  }

  public Slice<AlarmResponseDTO> loadAlarms(PaginationRequestDTO paginationRequestDTO){
    Pageable pageable = PageRequest.of(paginationRequestDTO.getSkip() / paginationRequestDTO.getLimit(),
        paginationRequestDTO.getLimit());

    int memberId = SecurityUtil.getCurrentUserId();

    Slice<Alarm> alarm = alarmRepository.findByMemberId(memberId, pageable);

    return alarm.map(AlarmMapper::toDto);
  }


}
