package com.todoslave.feedme.service;


import com.todoslave.feedme.DTO.CreatureInfoResponseDTO;
import com.todoslave.feedme.domain.entity.avatar.Creature;

public interface CreatureService {


    //크리쳐 만들기
    Creature createFristCreature(String keyword, String photo, String creatureName);
    //크리쳐 정보 가져오기
    CreatureInfoResponseDTO creatureInfo();
    //크리쳐 삭제하기
    boolean removeCreature();
    //크리쳐 경험치 올리기
    void expUp(int toDoCnt);



}
