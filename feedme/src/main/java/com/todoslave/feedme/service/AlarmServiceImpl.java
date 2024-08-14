package com.todoslave.feedme.service;


import com.todoslave.feedme.DTO.AlarmResponseDTO;
import com.todoslave.feedme.DTO.AlarmSetRequestDTO;
import com.todoslave.feedme.DTO.FriendReqResponseDTO;
import com.todoslave.feedme.DTO.MemberChatListResponseDTO;
import com.todoslave.feedme.domain.entity.alarm.Alarm;
import com.todoslave.feedme.domain.entity.avatar.Creature;
import com.todoslave.feedme.domain.entity.membership.Member;
import com.todoslave.feedme.domain.entity.membership.MemberAlarm;
import com.todoslave.feedme.login.util.SecurityUtil;
import com.todoslave.feedme.mapper.AlarmMapper;
import com.todoslave.feedme.repository.AlarmRepository;
import com.todoslave.feedme.repository.CreatureRepository;
import com.todoslave.feedme.repository.MemberAlarmRepository;
import com.todoslave.feedme.repository.MemberChatRoomCheckedRepository;
import com.todoslave.feedme.repository.MemberRepository;
import com.todoslave.feedme.repository.TodoRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@Service
public class AlarmServiceImpl implements AlarmService{

  private final AlarmRepository alarmRepository;
  private final MemberAlarmRepository memberAlarmRepository;
  private final TodoRepository todoRepository;
  private final MemberRepository memberRepository;
  private final CreatureRepository creatureRepository;
  private final MemberChatRoomCheckedRepository memberChatRoomCheckedRepository;

  private final Map<Integer, SseEmitter> emitters = new ConcurrentHashMap<>();
  private final Map<Integer, SseEmitter> chatEmitters = new ConcurrentHashMap<>();
  private final Map<Integer, SseEmitter> friendEmitters = new ConcurrentHashMap<>();

  @Override
  public SseEmitter friendCreateEmitter() {

    int memberId = SecurityUtil.getCurrentUserId();

    SseEmitter emitter = new SseEmitter();
    friendEmitters.put(memberId, emitter);

    System.out.println("friend subscribe open!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+memberId);

    if(friendEmitters.get(memberId)!=null){
      System.out.println("friend subscribe has!!!");
    }

    emitter.onCompletion(() -> friendEmitters.remove(memberId));
    emitter.onTimeout(() -> friendEmitters.remove(memberId));
    emitter.onError((e) -> friendEmitters.remove(memberId));

    return emitter;
  }

  @Override
  public void createAlarmtime(AlarmSetRequestDTO alarmSetRequestDTO) {

    Member member = SecurityUtil.getCurrentMember();

    MemberAlarm memberAlarm = new MemberAlarm();

    memberAlarm.setMember(member);
    memberAlarm.setAlarmTime(alarmSetRequestDTO.getAlarmTime());

    memberAlarm = memberAlarmRepository.save(memberAlarm);

  }

  @Override
  public SseEmitter createEmitter() {

    int memberId = SecurityUtil.getCurrentUserId();

    SseEmitter emitter = new SseEmitter();
    emitters.put(memberId, emitter);

    System.out.println("구독 열려용!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+memberId);

    emitter.onCompletion(() -> emitters.remove(memberId));
    emitter.onTimeout(() -> emitters.remove(memberId));
    emitter.onError((e) -> emitters.remove(memberId));

    return emitter;
  }

  //1시간 마다 확인, 일정 완료 했는지 여부
  @Scheduled(cron = "0 0 * * * ?")
  public void todoCompleted() throws IOException {

    LocalDate currentDay = LocalDate.now();
    List<Integer> members = todoRepository.findMemberIdAllByCreatedAtAndIsCompleted(currentDay, 0);

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

      sendAlarm(alarm, 0, emitters.get(memberId));

    }
  }

  //친구 요청 알림
  public void requestFriendship(FriendReqResponseDTO friendReqResponseDTO, int memberId) throws IOException {

    sendAlarm(friendReqResponseDTO,1, friendEmitters.get(memberId));

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
      sendAlarm(alarm, 0, emitters.get(member.getId()));

    }

  }

  // 채팅방 목록 페이지 들어왔을 때
  @Override
  public SseEmitter renewCreateEmitter() {
    int memberId = SecurityUtil.getCurrentUserId();

    System.out.println("Chatting renew channel is!!!!!!!!!!!!!! "+memberId);

    SseEmitter emitter = new SseEmitter();
    chatEmitters.put(memberId, emitter);

    emitter.onCompletion(() -> chatEmitters.remove(memberId));
    emitter.onTimeout(() -> chatEmitters.remove(memberId));
    emitter.onError((e) -> chatEmitters.remove(memberId));

    return emitter;
  }

  // 채팅방 갱신
  @Override
  @Transactional
  public void renewChattingRoom(MemberChatListResponseDTO room, int memberId, int checked) throws IOException {

    SseEmitter emitter = chatEmitters.get(memberId);

    System.out.println("enter the renew chat !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

    if(emitter!=null) {

      System.out.println(memberId+ "  renew chat SSE success!!!!!");

      room.setIsChecked(checked);

      SseEmitter.SseEventBuilder event = SseEmitter.event()
          .name("chattingRoom")
          .data(room);

      emitter.send(event);
    }else{
      System.out.println("Sse Connection fail");
    }

  }

  // 알람 받아라
  @Override
  public <T> void sendAlarm(T alarm, int type, SseEmitter emitter) throws IOException {

    System.out.println("alarm send!!!!!!!!!!"+alarm);

    if(emitter!=null){

      if(type==0) {
        AlarmResponseDTO dto = AlarmMapper.toDto((Alarm) alarm);

        SseEmitter.SseEventBuilder event = SseEmitter.event()
            .name("alarm")
            .data(dto);

        try {
          emitter.send(event);
        }catch (IOException e){
          emitter.completeWithError(e);
          throw e;
        }catch (Exception e){
          emitter.completeWithError(e);
          throw new RuntimeException("알람전송중 문제 발생",e);
        }
      }else if(type==1){

        SseEmitter.SseEventBuilder event = SseEmitter.event()
            .name("friend")
            .data(alarm);

        try {
          emitter.send(event);
        }catch (IOException e){
          emitter.completeWithError(e);
          throw e;
        }catch (Exception e){
          emitter.completeWithError(e);
          throw new RuntimeException("알람전송중 문제 발생",e);
        }
      }

    }else{
      System.out.println("Sse connect fail");
    }

  }

  public List<AlarmResponseDTO> loadAlarms(){

    int memberId = SecurityUtil.getCurrentUserId();

    List<Alarm> alarm = alarmRepository.findByMemberId(memberId);
    List<AlarmResponseDTO> response = new ArrayList<>();

    for(Alarm a : alarm){

      AlarmResponseDTO alarmResponseDTO = AlarmMapper.toDto(a);
      response.add(alarmResponseDTO);

    }

    return response;
  }

  @Override
  @Transactional
  public void deleteAlarm(int alarmId) {

    alarmRepository.deleteById(alarmId);

  }


}
