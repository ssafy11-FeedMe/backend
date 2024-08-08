package com.todoslave.feedme.controller;

import com.todoslave.feedme.DTO.AlarmCheckRequestDTO;
import com.todoslave.feedme.DTO.PaginationRequestDTO;
import com.todoslave.feedme.domain.entity.alarm.Alarm;
import com.todoslave.feedme.login.util.SecurityUtil;
import com.todoslave.feedme.service.AlarmService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alarms")
public class AlarmController {

    private AlarmService alarmService;

    @GetMapping(value = "/subscribe/alarm")
    public SseEmitter subscribe(){
        return alarmService.createEmitter();
    }

    @GetMapping(value = "/subscribe/friend")
    public SseEmitter friendSubscribe(){
        return alarmService.friendCreateEmitter();
    }

    @GetMapping(value = "/subscribe/chat")
    public SseEmitter chatSubscribe(){
        return alarmService.renewCreateEmitter();
    }

    // 생일, 투두
    @GetMapping()
    private ResponseEntity<Slice<Alarm>> roadAlarms(PaginationRequestDTO paginationRequestDTO) {
        return ResponseEntity.ok(alarmService.roadAlarms(paginationRequestDTO));
    }


}
