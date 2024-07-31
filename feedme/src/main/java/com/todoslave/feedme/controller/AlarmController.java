package com.todoslave.feedme.controller;

import com.todoslave.feedme.DTO.AlarmRequest;
import com.todoslave.feedme.domain.entity.check.Alarm;
import com.todoslave.feedme.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alarm")
public class AlarmController {

    private AlarmService alarmService;

    private final Map<Integer, SseEmitter> emitters = new ConcurrentHashMap<>();

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestHeader String token){

        //token을 받아서 memberid를 찾음
        int memberId;

        SseEmitter emitter = new SseEmitter();
        emitters.put(memberId, emitter);
        emitter.onCompletion(()->emitters.remove(memberId));
        emitter.onTimeout(()->emitters.remove(memberId));
        return emitter;

    }

    private void sendAlarmToMember(int memberId, String message){

        SseEmitter emitter = emitters.get(memberId);
        if(emitter != null){
            try {
                emitter.send(SseEmitter.event().name("Alarm").data(message));
            }
        }


    }



}
