package com.todoslave.feedme.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CreatureMakeRequestDTO {

    String creatureName;

    String keyword;

    // AI 구현시 이걸로 바꿔야함
     MultipartFile photo;
//    String photo;

}
