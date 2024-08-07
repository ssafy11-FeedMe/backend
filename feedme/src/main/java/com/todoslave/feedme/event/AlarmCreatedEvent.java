package com.todoslave.feedme.event;

import com.todoslave.feedme.DTO.MemberChatRenewResponseDTO;
import com.todoslave.feedme.domain.entity.alarm.Alarm;
import org.springframework.context.ApplicationEvent;

public class AlarmCreatedEvent extends ApplicationEvent {

  private Alarm alarm;
  private MemberChatRenewResponseDTO memberChatRenewResponseDTO;
  private String alarmType;

  public AlarmCreatedEvent(Object source, Alarm alarm, String type) {
    super(source);
    this.alarm = alarm;
    this.alarmType = type;
  }

  public AlarmCreatedEvent(Object source, MemberChatRenewResponseDTO memberChatRenewResponseDTO, String type){
    super(source);
    this.memberChatRenewResponseDTO = memberChatRenewResponseDTO;
    this.alarmType = type;
  }

  public MemberChatRenewResponseDTO getRoom(){
    return memberChatRenewResponseDTO;
  }

  public Alarm getAlarm(){
    return alarm;
  }

  public String getAlarmType(){
    return alarmType;
  }

}
