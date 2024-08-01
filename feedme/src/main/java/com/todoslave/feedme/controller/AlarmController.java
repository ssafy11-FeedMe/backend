package com.todoslave.feedme.controller;

import com.todoslave.feedme.DTO.AlarmCheckRequestDTO;
import com.todoslave.feedme.DTO.AlarmCheckServiceDTO;
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

    private final Map<Integer, SseEmitter> emitters = new ConcurrentHashMap<>();

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestHeader String token){

        //token을 받아서 memberid를 찾음
        int memberId = 0;

        SseEmitter emitter = new SseEmitter();
        emitters.put(memberId, emitter);
        emitter.onCompletion(()->emitters.remove(memberId));
        emitter.onTimeout(()->emitters.remove(memberId));
        return emitter;

    }

    private void sendAlarmToMember(int memberId, String message) throws Exception{

        SseEmitter emitter = emitters.get(memberId);
        if(emitter != null){
          emitter.send(SseEmitter.event().name("Alarm").data(message));
        }

    }

    @RequestMapping("/read")
    private void checkAlarm(@RequestBody AlarmCheckRequestDTO alarmCheckRequestDTO){

      LocalDateTime checkTime = LocalDateTime.parse(alarmCheckRequestDTO.getCheckTime(), DateTimeFormatter.ISO_DATE_TIME);
      AlarmCheckServiceDTO alarmCheckServiceDTO = new AlarmCheckServiceDTO();
      alarmCheckServiceDTO.setCheckTime(checkTime);


    }


}
