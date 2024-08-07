package com.todoslave.feedme.controller;

import com.todoslave.feedme.DTO.AlarmCheckRequestDTO;
import com.todoslave.feedme.login.util.SecurityUtil;
import com.todoslave.feedme.service.AlarmService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alarms")
public class AlarmController {

    private AlarmService alarmService;
    private SecurityUtil securityUtil;


    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(){
        return alarmService.createEmitter();
    }

    private void sendAlarmToMember(String message) throws Exception{

        int memberId = securityUtil.getId();

        SseEmitter emitter = emitters.get(memberId);
        if(emitter != null){
          emitter.send(SseEmitter.event().name("Alarm").data(message));
        }

    }

    @RequestMapping("/read")
    private void checkAlarm(@RequestBody AlarmCheckRequestDTO alarmCheckRequestDTO){

        LocalDateTime checkTime = LocalDateTime.parse(alarmCheckRequestDTO.getCheckTime(), DateTimeFormatter.ISO_DATE_TIME);
        alarmService.checkAlarm(checkTime);

    }


}
