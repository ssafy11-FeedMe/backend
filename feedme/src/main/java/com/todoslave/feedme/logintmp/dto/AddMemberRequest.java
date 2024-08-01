package com.todoslave.feedme.logintmp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
public class AddMemberRequest {
    private String email;
    private String nickname;
    private Timestamp birthday;
    private Double latitude;
    private Double longitude;
}
