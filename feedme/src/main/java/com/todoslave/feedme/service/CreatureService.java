package com.todoslave.feedme.service;


import com.todoslave.feedme.DTO.CreatureInfoResponseDTO;
import com.todoslave.feedme.domain.entity.avatar.Creature;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;

public interface CreatureService {


    Creature createCreature();


//    Creature createFristCreature(String keyword, MultipartFile photo, String creatureName);
    Creature createFristCreature(String keyword, String photo, String creatureName);
    CreatureInfoResponseDTO CreatureInfo();

}
