package com.todoslave.feedme.controller;

import com.todoslave.feedme.DTO.CreatureInfoResponseDTO;
import com.todoslave.feedme.DTO.CreatureMakeRequest;
import com.todoslave.feedme.domain.entity.avatar.Creature;
import com.todoslave.feedme.domain.entity.membership.Member;
import com.todoslave.feedme.login.util.SecurityUtil;
import com.todoslave.feedme.service.CreatureService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/creature")
public class CreatureController {

    private final CreatureService creatureService;

    @Autowired
    public CreatureController(CreatureService creatureService) {
        this.creatureService = creatureService;
    }

    @Operation(summary = "크리쳐 생성")
    @PostMapping
    public ResponseEntity<?> createCreature(@RequestBody CreatureMakeRequest request, @RequestHeader("Authorization") final String accessToken) {
        Member member = SecurityUtil.getCurrentMember();

            Creature creature = creatureService.createFristCreature(request.getKeyword(), request.getPhoto(), request.getCreatureName());

        return ResponseEntity.ok(Map.of("creatureId", creature.getId(), "message", "크리쳐가 성공적으로 생성되었습니다."));
    }

//    @Operation(summary = "크리쳐 생성 미리 만든 버젼")
//    @PostMapping("/make")
//    public ResponseEntity<?> createCreature2(@RequestBody CreatureMakeRequest request, @RequestHeader("Authorization") final String accessToken) {
//        Member member = SecurityUtil.getCurrentMember();
//
//        Creature creature = creatureService.createFristCreature(request.getKeyword(), request.getPhoto(), request.getCreatureName());
//
//        return ResponseEntity.ok(Map.of("creatureId", creature.getId(), "message", "크리쳐가 성공적으로 생성되었습니다2"));
//    }

    @Operation(summary = "크리쳐 보기")
    @GetMapping
    public ResponseEntity<CreatureInfoResponseDTO> getCreatures(@RequestHeader("Authorization") final String accessToken) {
        CreatureInfoResponseDTO creature = creatureService.CreatureInfo();
        return new ResponseEntity<CreatureInfoResponseDTO>(creature, HttpStatus.OK);
    }


}
