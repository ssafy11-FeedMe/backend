package com.todoslave.feedme.controller;

import com.todoslave.feedme.DTO.AlarmResponseDTO;
import com.todoslave.feedme.login.util.SecurityUtil;
import com.todoslave.feedme.DTO.PaginationRequestDTO;
import com.todoslave.feedme.domain.entity.alarm.Alarm;
import com.todoslave.feedme.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alarms")
public class AlarmController {

    private final SecurityUtil securityUtil;
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
    private ResponseEntity<Slice<AlarmResponseDTO>> loadAlarms(PaginationRequestDTO paginationRequestDTO) {
        return ResponseEntity.ok(alarmService.loadAlarms(paginationRequestDTO));
    }


}
